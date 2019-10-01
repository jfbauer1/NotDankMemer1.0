/*
 * Developed by Turulix on 21.01.19 21:18.
 * Last modified 21.01.19 15:42.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.commons;

/**
 * Information regarding the library.
 * <br>Visit the JDA-Utilities <a href="https://github.com/JDA-Applications/JDA-Utilities">GitHub Repository</a>
 * to submit issue reports or feature requests, or join the <a href="https://discord.gg/0hMr4ce0tIk3pSjp"> Official JDA
 * Discord Guild</a> if you need any assistance with the library!
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public final class JDAUtilitiesInfo {
    public static final String VERSION_MAJOR;
    public static final String VERSION_MINOR;
    public static final String VERSION;
    public static final String GITHUB = "https://github.com/JDA-Applications/JDA-Utilities";
    public static final String AUTHOR = "JDA-Applications";

    // Version init block
    static {
        Package pkg = JDAUtilitiesInfo.class.getPackage();

        String version = pkg.getImplementationVersion();
        VERSION = version == null ? "Custom" : version;

        String[] parts = VERSION.split("\\.", 2);
        VERSION_MAJOR = version == null ? "2" : parts[0]; // This should only be updated every version major!
        VERSION_MINOR = version == null ? "X" : parts[1];
    }
}
