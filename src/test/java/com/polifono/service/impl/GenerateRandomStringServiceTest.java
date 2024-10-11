package com.polifono.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GenerateRandomStringServiceTest {

    @InjectMocks
    private GenerateRandomStringService generateRandomStringService;

    @Test
    public void givenLength_whenGenerateRandomString_thenLengthIsCorrect() {
        String randomString10 = generateRandomStringService.generate(10);
        assert (randomString10.length() == 10);

        String randomString6 = generateRandomStringService.generate(6);
        assert (randomString6.length() == 6);

        String randomString1 = generateRandomStringService.generate(1);
        assert (randomString1.length() == 1);
    }

    @Test
    public void givenLength_whenGenerateRandomStringMultipleTimes_thenGenerateDifferentStrings() {
        String randomString1 = generateRandomStringService.generate(10);
        assert (randomString1.length() == 10);

        String randomString2 = generateRandomStringService.generate(10);
        assert (randomString2.length() == 10);

        assert (!randomString1.equals(randomString2));
    }

    @Test
    public void givenLength_whenGenerateRandomStringWithLengthLessThanOne_thenEmptyString() {
        String randomString = generateRandomStringService.generate(0);
        assert (randomString.isEmpty());

        randomString = generateRandomStringService.generate(-1);
        assert (randomString.isEmpty());
    }
}
