/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.doc.standard;

import java.lang.annotation.*;

/**
 * The {@link java.lang.annotation.Repeatable @Repeatable} value for {@link com.jagrosh.jdautilities.doc.standard.Error
 *
 * @author Kaidan Gustave
 * @Error}. <br>Useful for organizing multiple @Error annotations
 * @see Error
 * @since 2.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Errors {
    /**
     * One or more {@link com.jagrosh.jdautilities.doc.standard.Error @Error} annotations.
     *
     * @return One or more @Error annotations
     */
    Error[] value();
}
