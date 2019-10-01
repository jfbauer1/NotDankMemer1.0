/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.UtilClasses.Anotations;

import java.lang.annotation.*;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 06.01.2019 09:58
 */


/**
 * Marks a class as a NotDankMemer {@link com.jagrosh.jdautilities.command.Command Command}
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DankCommand {

}
