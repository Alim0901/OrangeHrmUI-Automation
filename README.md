# Selenium Cucumber Maven Framework

This project is a test automation framework using Java, Selenium WebDriver, Cucumber, and Maven.
This framework is built using Java 17 and is compatible with Java 17 or later versions.
## 📁 Project Structure

- `src/test/java` - Step definitions, pages, utils, runner.TestSuite
- `src/test/resources` - Feature files, config, test data
- `pom.xml` - Maven dependencies and plugins

## 🚀 Tools & Libraries

| Tool/Library         | Purpose                                                    |
|----------------------|------------------------------------------------------------|
| Java 24              | Core programming language                                  |
| Maven                | Build & dependency management                              |
| Playwright for Java  | UI automation                                              |
| Cucumber             | BDD and feature file execution                             |
| JUnit                | Test runner                                                |
| ExtentReports        | Rich HTML reports with screenshots & Allure reports for CI |


---

## ⚙️ Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd <project-folder>
2. **Install java 24 0r above version**
3. **Install Cucumber Java & Gherkin Plugins from IDE settings**

## 🧪 Running Tests

Tests can be executed in two ways:

1. **From within the feature file**
-  Right-click anywhere inside a .feature file in IntelliJ and select "Run" to execute just that scenario or feature.
2. **Using Tags via the Test Runner class**
- Scenarios can be grouped and executed using tags like @smoke, @regression, etc.
- The @CucumberOptions in runner.TestSuite.java handles tag-based execution.

## 📄 Reports

After test execution:
- `target/ExtentReport.html` – Extent HTML report with step-level screenshots
- `target/cucumber-html-report.html` – Cucumber default report
- `target/cucumber.json` – JSON format for CI/CD tools
- `target/cucumber-results.xml` – JUnit-compatible XML