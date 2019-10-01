/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.doc;

import java.lang.annotation.*;

/**
 * Specifies an {@link java.lang.annotation.Annotation Annotation} can be converted using the specified {@link
 * com.jagrosh.jdautilities.doc.DocConverter DocConverter} value.
 *
 * <p>Only annotations with this annotation applied to it are valid for processing
 * via an instance of {@link com.jagrosh.jdautilities.doc.DocGenerator DocGenerator}.
 *
 * @author Kaidan Gustave
 * @see DocConverter
 * @since 2.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ConvertedBy {
    /**
     * The {@link com.jagrosh.jdautilities.doc.DocConverter DocConverter} Class that the annotation this is applied to
     * provides to {@link com.jagrosh.jdautilities.doc.DocConverter#read(Annotation) DocConverter#read(Annotation)}.
     *
     * @return The DocConverter Class to use.
     */
    Class<? extends DocConverter> value();
}
