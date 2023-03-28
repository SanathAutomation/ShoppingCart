package com.test.cucumber;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features = { "src/test/resources/Sanity/test.feature" } , tags = {
"@tag1" })
public class SanityRunner  { }

