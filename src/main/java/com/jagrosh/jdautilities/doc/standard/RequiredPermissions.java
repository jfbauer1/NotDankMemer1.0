/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.doc.standard;

import com.jagrosh.jdautilities.doc.ConvertedBy;
import com.jagrosh.jdautilities.doc.DocConverter;
import net.dv8tion.jda.core.Permission;

import java.lang.annotation.*;

/**
 * A CommandDoc {@link java.lang.annotation.Annotation Annotation} that lists required {@link
 * net.dv8tion.jda.core.Permission Permission}s a bot must have to use a command on a {@link
 * net.dv8tion.jda.core.entities.Guild Guild}.
 *
 * @author Kaidan Gustave
 * @since 2.0
 */
@ConvertedBy(RequiredPermissions.Converter.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequiredPermissions {
    /**
     * An array of {@link net.dv8tion.jda.core.Permission Permission}s a bot must have to run the command.
     *
     * @return The array of permissions
     */
    Permission[] value();

    /**
     * The {@link com.jagrosh.jdautilities.doc.DocConverter DocConverter} for the {@link
     * com.jagrosh.jdautilities.doc.standard.RequiredPermissions @RequiredPermissions} annotation.
     */
    class Converter implements DocConverter<RequiredPermissions> {
        @Override
        public String read(RequiredPermissions annotation) {
            Permission[] permissions = annotation.value();

            StringBuilder b = new StringBuilder();

            b.append("Bot must have permissions:");
            switch (permissions.length) {
                case 0:
                    b.append(" None");
                    break;
                case 1:
                    b.append(" `").append(permissions[0].getName()).append("`");
                    break;
                default:
                    for (int i = 0; i < permissions.length; i++) {
                        b.append(" `").append(permissions[i]).append("`");
                        if (i != permissions.length - 1) b.append(",");
                    }
                    break;
            }
            return b.toString();
        }
    }
}
