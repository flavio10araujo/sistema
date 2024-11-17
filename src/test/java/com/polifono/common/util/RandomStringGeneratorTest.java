package com.polifono.common.util;

import org.junit.jupiter.api.Test;

public class RandomStringGeneratorTest {

    @Test
    public void givenLength_whenGenerateRandomString_thenLengthIsCorrect() {
        String randomString10 = RandomStringGenerator.generate(10);
        assert (randomString10.length() == 10);

        String randomString6 = RandomStringGenerator.generate(6);
        assert (randomString6.length() == 6);

        String randomString1 = RandomStringGenerator.generate(1);
        assert (randomString1.length() == 1);
    }

    @Test
    public void givenLength_whenGenerateRandomStringMultipleTimes_thenGenerateDifferentStrings() {
        String randomString1 = RandomStringGenerator.generate(10);
        assert (randomString1.length() == 10);

        String randomString2 = RandomStringGenerator.generate(10);
        assert (randomString2.length() == 10);

        assert (!randomString1.equals(randomString2));
    }

    @Test
    public void givenLength_whenGenerateRandomStringWithLengthLessThanOne_thenEmptyString() {
        String randomString = RandomStringGenerator.generate(0);
        assert (randomString.isEmpty());

        randomString = RandomStringGenerator.generate(-1);
        assert (randomString.isEmpty());
    }
}
