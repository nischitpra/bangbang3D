package com.nhuchhe.bangbang;

public class Logger {
    public static void log(String s) {
        System.out.println(s);
    }

    public static void log(String s, Object... args) {
        System.out.println(String.format(s, args));
    }
}
