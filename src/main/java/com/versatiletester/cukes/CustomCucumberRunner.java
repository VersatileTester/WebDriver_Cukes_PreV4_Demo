package com.versatiletester.cukes;

import cucumber.api.junit.Cucumber;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import com.versatiletester.cukes.runners.RunCukesTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * A custom Cucumber runner class, used to implement the 
 * Before/After Suite classes and functionality.
 * 
 * @see AfterSuite
 * @see BeforeSuite
 * @see RunCukesTest
 * @see Cucumber
 */
@SuppressWarnings("rawtypes")
public class CustomCucumberRunner extends Runner {
    
	private Class clazz;
    
    private Cucumber cucumber;

    public CustomCucumberRunner(Class clazzValue) throws Exception {
        clazz = clazzValue;
        cucumber = new Cucumber(clazzValue);
    }

    @Override
    public Description getDescription() {
        return cucumber.getDescription();
    }

    private void runPredefinedMethods(Class annotation) throws Exception {
        if (!annotation.isAnnotation()) {
            return;
        }
        Method[] methodList = this.clazz.getMethods();
        for (Method method : methodList) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation item : annotations) {
                if (item.annotationType().equals(annotation)) {
                    method.invoke(null);
                    break;
                }
            }
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            runPredefinedMethods(BeforeSuite.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cucumber.run(notifier);
        try {
            runPredefinedMethods(AfterSuite.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}