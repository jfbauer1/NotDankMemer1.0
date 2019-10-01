/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.examples.doc;

import com.jagrosh.jdautilities.doc.ConvertedBy;
import com.jagrosh.jdautilities.doc.DocConverter;

import java.lang.annotation.*;

/**
 * Annotation to mark a command's specific author.
 *
 * @author Kaidan Gustave
 * @since 2.0
 */
@ConvertedBy(Author.Converter.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Author {
    String value();

    class Converter implements DocConverter<Author> {
        @Override
        public String read(Author annotation) {
            return String.format("**Author:** %s", annotation.value());
        }
    }
}
