package com.alliander.osgp.platform.cucumber;

/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/", tags = { "@SLIM-392, @SLIM-227, @SLIM-213" }, plugin = { "pretty",
        "html:target/output/Cucumber-report", "html:target/output/Cucumber-html-report.html" }, snippets = SnippetType.CAMELCASE)
public class RunCukesTest {

}
