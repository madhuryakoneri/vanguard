package com.vangaurd.tradereporting.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class AnagramUtil {
    public static boolean isAnagram(String s1, String s2) {

        if (s1 == null || s2 == null || (s1.length() != s2.length())) return false;
        var chars1 = s1.toLowerCase().toCharArray();
        var chars2 = s2.toLowerCase().toCharArray();

        Arrays.sort(chars1);
        Arrays.sort(chars2);
        return Arrays.equals(chars1, chars2);
    }
}
