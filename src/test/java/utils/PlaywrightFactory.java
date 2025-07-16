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
        String  browserCfg = XMLConfigLoader.get("browser");        // chrome / chromium / firefox / webkit
        boolean headless   = XMLConfigLoader.getBoolean("headless");
        String  size       = XMLConfigLoader.get("windowSize");     // e.g. 1920,1080
        int     width      = Integer.parseInt(size.split(",")[0].trim());
        int     height     = Integer.parseInt(size.split(",")[1].trim());

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
        Browser.NewContextOptions ctxOpts = new Browser.NewContextOptions()
                .setViewportSize(width, height);              // works across all engines

        tlContext.set(browser.newContext(ctxOpts));
        tlPage.set(tlContext.get().newPage());

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
