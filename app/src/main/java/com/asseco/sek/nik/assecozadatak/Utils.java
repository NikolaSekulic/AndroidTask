package com.asseco.sek.nik.assecozadatak;

import android.content.Context;

/**
 * Helper methods and constants.
 * Created by sekul on 21.12.2015..
 */
public class Utils {


    /**
     * Siye of buffer taht reads content from web.
     */
    public static final int BUFFER_SIZE = 1025;

    /**
     * Algorithm type for checksum calculation.
     */
    public static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Period for disabeling button.
     */
    public static final int TIMER_PERIOD = 5000;

    /**
     * Checks if first digit of hash is even.
     *
     * @param hash    hahs in hex string notation.
     * @param context context of application.
     * @return true if and only if first digit of hash is even.
     * @throws IllegalArgumentException if ivalid hash is provided/
     */
    public static boolean firstDigitEven(String hash, Context context) throws IllegalArgumentException {
        if (hash == null || hash.length() < 2) {
            throw new IllegalArgumentException(context.getString(R.string.invalid_hash)
                    + " " + hash);
        }

        String firstByteString = hash.substring(0, 2);

        int firstByte = 0;

        try {
            firstByte = Integer.parseInt(firstByteString, 16);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(context.getString(R.string.invalid_hash)
                    + " " + hash);
        }

        return firstByte % 2 == 0;
    }
}
