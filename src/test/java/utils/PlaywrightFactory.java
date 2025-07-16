package utils;

import com.microsoft.playwright.*;

public class PlaywrightFactory {

    /* ────────── Thread‑local containers ────────── */
    private static final ThreadLocal<Playwright>     tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<Browser>        tlBrowser    = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> tlContext    = new ThreadLocal<>();
    private static final ThreadLocal<Page>           tlPage       = new ThreadLocal<>();

    /* ────────── Public API ────────── */

    /** Call once in a Cucumber @Before hook */
    public static Page initBrowser() {

        /* --- read config.xml --- */
        // chrome / chromium / firefox / webkit
        String  browserCfg = XMLConfigLoader.get("browser");
        boolean headless   = XMLConfigLoader.getBoolean("headless");
        String windowType = XMLConfigLoader.get("window"); // desktop / mobile / iphone etc.

        /* --- create Playwright & browser --- */
        tlPlaywright.set(Playwright.create());

        BrowserType.LaunchOptions launchOpts = new BrowserType.LaunchOptions()
                .setHeadless(headless);                       // no --window-size flag now

        Browser browser = switch (browserCfg.toLowerCase()) {
            case "firefox"            -> tlPlaywright.get().firefox().launch(launchOpts);
            case "webkit", "safari"   -> tlPlaywright.get().webkit().launch(launchOpts);
            case "chromium", "chrome" -> tlPlaywright.get().chromium()
                    .launch(launchOpts.setChannel(
                            browserCfg.equalsIgnoreCase("chrome") ? "chrome" : null));
            default -> throw new IllegalArgumentException("Unsupported browser in config.xml: " + browserCfg);
        };

        tlBrowser.set(browser);

        /* --- create context & page with unified viewport --- */
        Browser.NewContextOptions ctxOpts = new Browser.NewContextOptions();
        // works across all engines
        if (headless) {
            // ✅ Always use default CI-safe resolution in headless mode
            ctxOpts.setViewportSize(1366, 768);
            System.out.println("[INFO] Headless mode → using default viewport: 1366x768");
        } else {
            // ✅ Headed mode → use config value
            if (windowType == null || windowType.isBlank()) {
                System.out.println("[INFO] No window type provided → using default (desktop)");
            } else {
                switch (windowType.toLowerCase()) {
                    case "desktop" -> {
                        // No viewport set → launches full-size in headed mode
                        System.out.println("[INFO] Window = desktop → no viewport set (uses native resolution)");
                    }
                    case "mobile", "iphone" -> {
                        ctxOpts.setViewportSize(390, 844); // iPhone 13 dimensions
                        System.out.println("[INFO] Window = mobile → using 390x844");
                    }
                    case "pixel" -> {
                        ctxOpts.setViewportSize(412, 915); // Pixel 6
                        System.out.println("[INFO] Window = pixel → using 412x915");
                    }
                    default -> {
                        System.out.printf("[WARN] Unknown window type: %s → defaulting to desktop%n", windowType);
                    }
                }
            }
        }
        tlContext.set(browser.newContext(ctxOpts));
        tlPage.set(tlContext.get().newPage());

        System.out.printf("[INFO] Browser: %s | Headless: %s | Window: %s%n", browserCfg, headless, windowType);
        return tlPage.get();
    }

    /** Retrieve the current thread’s Page (returns null if initBrowser() hasn’t run yet) */
    public static Page getPage() { return tlPage.get(); }

    /** Clean up; call in a Cucumber @After hook */
    public static void closeBrowser() {
        if (tlContext.get()    != null) tlContext.get().close();
        if (tlBrowser.get()    != null) tlBrowser.get().close();
        if (tlPlaywright.get() != null) tlPlaywright.get().close();
        tlPage.remove();
        tlContext.remove();
        tlBrowser.remove();
        tlPlaywright.remove();
    }
}
