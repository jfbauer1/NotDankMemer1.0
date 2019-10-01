/*
 * Developed by Turulix on 22.01.19 00:20.
 * Last modified 21.01.19 23:43.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 21.01.2019 18:46
 */
public class I18n {
    private static String baseName = "lang";

    public static ResourceBundle getResourceBundle(Locale language) {
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, language);
        return bundle;
    }

}
