package com.frc107.vanguard.core.utils;

public class StringUtils {
    private StringUtils() {}

    public static boolean isEmptyOrNull(String string){
        return string == null || string.isEmpty();
    }
}