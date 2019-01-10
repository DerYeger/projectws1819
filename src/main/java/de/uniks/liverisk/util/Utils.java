package de.uniks.liverisk.util;

import java.util.Objects;

public class Utils {

    public static String hexColorStringToWebColorString(String hexColorString) {
        Objects.requireNonNull(hexColorString);
        return '#' + hexColorString.substring(2, hexColorString.length() - 2);
    }
}
