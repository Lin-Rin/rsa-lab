package com.example.rsalab.util.math;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.function.Supplier;

@Component
public class PrimeNumberGenerator implements Supplier<Long> {
    @Override
    public Long get() {
        SecureRandom secureRandom = new SecureRandom();
        long candidate = secureRandom.nextLong();
        while (!checkIfPrime(candidate)) {
            candidate = secureRandom.nextLong();
        }
        return candidate;
    }

    private boolean checkIfPrime(Long number) {
        for (int i = 2; i < Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
