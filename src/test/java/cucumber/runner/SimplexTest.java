package cucumber.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/simplexTest/", plugin = { "pretty",
        "html:target/html-cucumber-report" ,
        "rerun:target/rerun.txt" }, glue = { "cucumber" }, tags = "@SimplexTest")
public class SimplexTest {
}
