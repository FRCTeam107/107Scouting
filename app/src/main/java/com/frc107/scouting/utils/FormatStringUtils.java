package com.frc107.scouting.utils;

import java.util.ArrayList;



public class FormatStringUtils {

    public static String addDelimiter(ArrayList<CharSequence> strings, String delimiter) {

        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < strings.size(); i++) {
            stringBuilder.append(strings.get(i));

            if (i < strings.size() - 1) {
                stringBuilder.append(delimiter);
            }
        }

        return stringBuilder.toString();
    }
}