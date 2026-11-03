package com.ogoma.marketing.core.sharedkernel;

import java.security.SecureRandom;
import java.util.UUID;

public class UUIDV7Generator {

    private UUIDV7Generator(){}

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a new UUID version 7.
     * <p>
     * Format:
     * - 48 bits: Unix timestamp in milliseconds
     * - 4 bits: Version (0111 for v7)
     * - 12 bits: Random data
     * - 2 bits: Variant (10)
     * - 62 bits: Random data
     *
     * @return a new UUIDv7
     */
    public static UUID generate() {
        long timestamp = System.currentTimeMillis();

        // Generate random bytes for the random portions
        byte[] randomBytes = new byte[10];
        RANDOM.nextBytes(randomBytes);

        // Build the most significant bits (MSB)
        // 48 bits of timestamp + 4 bits version + 12 bits random
        long msb = (timestamp << 16) | // 48 bits timestamp
                ((long) (randomBytes[0] & 0x0F) << 8) | // 4 bits after version (12 bits random start)
                ((long) randomBytes[1] & 0xFF); // 8 more bits random

        // Set version to 7 (0111 in binary)
        msb = (msb & 0xFFFFFFFFFFFF0FFFL) | 0x0000000000007000L;

        // Build the least significant bits (LSB)
        // 2 bits variant + 62 bits random
        long lsb = 0L;
        for (int i = 2; i < 10; i++) {
            lsb = (lsb << 8) | ((long) randomBytes[i] & 0xFF);
        }

        // Set variant to 10 (RFC 4122 variant)
        lsb = (lsb & 0x3FFFFFFFFFFFFFFFL) | 0x8000000000000000L;

        return new UUID(msb, lsb);
    }
}
