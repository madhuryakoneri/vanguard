package com.vangaurd.tradereporting.util;

import com.vangaurd.tradereporting.utils.AnagramUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnagramUtilTest {

    @Test
    public void testIsAnagram_SimpleCase() {
        String str1 = "listen";
        String str2 = "silent";
        assertTrue(AnagramUtil.isAnagram(str1, str2));
    }

    @Test
    public void testIsAnagram_NotAnagram() {
        String str1 = "hello";
        String str2 = "world";
        assertFalse(AnagramUtil.isAnagram(str1, str2));
    }

    @Test
    public void testIsAnagram_NullStrings() {
        String str1 = null;
        String str2 = null;
        assertFalse(AnagramUtil.isAnagram(str1, str2));
    }

    @Test
    public void testIsAnagram_DifferentLengthStrings() {
        String str1 = "hello";
        String str2 = "helloo";
        assertFalse(AnagramUtil.isAnagram(str1, str2));
    }
}
