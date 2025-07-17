package runner;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")                 // Required for Cucumber to be recognized by JUnit platform
@SelectClasspathResource("features")       // looks under src/test/resources/features
@ConfigurationParameter(
        key   = Constants.GLUE_PROPERTY_NAME,
        value = "stepdefinitions,utils"
)
@ConfigurationParameter(
        key   = Constants.PLUGIN_PROPERTY_NAME,
        value = "pretty,html:target/cucumber-html-report.html," +
                "json:target/cucumber.json," +
                "junit:target/cucumber-results.xml," +
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)

@ConfigurationParameter(
        key   = Constants.FILTER_TAGS_PROPERTY_NAME,
        value = "@smoke"
)
@ConfigurationParameter(
        key   = "cucumber.execution.parallel.enabled",
        value = "true"
)

public class TestSuite { }
