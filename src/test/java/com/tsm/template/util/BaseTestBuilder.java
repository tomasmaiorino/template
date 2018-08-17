package com.tsm.template.util;

import org.apache.commons.lang3.RandomStringUtils;

import static org.apache.commons.lang3.RandomStringUtils.random;

public class BaseTestBuilder {

    public static String getValidEmail() {
        return random(10, true, true) + "@" + random(4, true, true) + ".com";
    }

    public static String getInvalidEmail() {
        return random(10, true, true) + "@";
    }

    public static String getInvalidStatus() {
        return "STA";
    }

    public static String getString(final Integer size) {
        return random(size, true, true);
    }

    public static String getLargeString(final Integer valid) {
        return random(valid + 1, true, true);
    }

    public static String getSmallString(final Integer valid) {
        return random(valid - 1, true, true);
    }

    public static Integer getLargeInteger(final Integer valid) {
        return Integer.parseInt(RandomStringUtils.randomNumeric(valid + 1));
    }

    public static Integer getSmallInteger(final Integer valid) {
        return Integer.parseInt(RandomStringUtils.randomNumeric(valid - 1));
    }

    public static String getImgUrl() {
        return "http://" + random(20, true, false) + ".com";
    }
}
