/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Listeners;

import me.turulix.main.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 05.01.2019 11:56
 */

public class TestFailListener implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            @NotNull StringWriter sw = new StringWriter();
            context.getExecutionException().get().printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Logger.webHook(context.getTestClass().get().getSimpleName() + "." + context.getTestMethod().get().getName() + " Failed!", exceptionAsString, "Maven");
        }
    }
}
