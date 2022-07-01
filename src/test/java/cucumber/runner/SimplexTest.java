package cucumber.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/SimplexTest.feature", plugin = { "pretty",
        "html:target/html-cucumber-report" }, glue = { "cucumber" })
public class SimplexTest {
}
