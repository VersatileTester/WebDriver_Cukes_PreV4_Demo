package com.versatiletester.cukes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation Class to extend the cucumber framework's functionality 
 * to use the equivalent of @AfterAll in Junit. Implemented within 
 * the RunCukesTest class.
 * 
 * @see CustomCucumberRunner
 * @see BeforeSuite
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD })
public @interface AfterSuite {}