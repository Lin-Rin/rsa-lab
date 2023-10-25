package com.example.rsalab.util.math;

import org.springframework.stereotype.Component;
import java.math.BigInteger;
import java.util.function.Predicate;

@Component
public class SimplicityTest implements Predicate<BigInteger> {
    @Override
    public boolean test(BigInteger number) {
        return false;
    }
}
