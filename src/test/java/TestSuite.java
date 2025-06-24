import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "html:target/cucumber-html-report.html",
                "json:target/cucumber.json",
                "pretty",
                "junit:target/cucumber-results.xml"
        },
        features = {"src/test/resources/features"},
        glue = {"stepdefinitions", "utils"},
        tags = "@smoke"
)
public class TestSuite {
}