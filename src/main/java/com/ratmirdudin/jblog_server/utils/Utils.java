package com.ratmirdudin.jblog_server.utils;

import java.security.SecureRandom;
import java.util.Random;

public class Utils {

    private static final String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateSecureRandomString(int n) {

        int length = symbols.length();
        Random random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < n; ++i) {
            stringBuilder.append(symbols.charAt(random.nextInt(length)));
        }

        return new String(stringBuilder);
    }
}
