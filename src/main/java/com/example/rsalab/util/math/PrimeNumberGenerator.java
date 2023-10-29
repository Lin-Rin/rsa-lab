package com.example.rsalab.util.math;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.function.Supplier;
import io.github.kosssst.asymcryptolab1.generators.L20Generator;

@Component
public class PrimeNumberGenerator implements Supplier<BigInteger> {
    private final int length;

    public PrimeNumberGenerator(int length) {
        this.length = length;
    }

    @Override
    public BigInteger get() {
        L20Generator generator = new L20Generator();
        BigInteger number = new BigInteger(generator.generate(this.length), 2);
        return number;
    }
}
