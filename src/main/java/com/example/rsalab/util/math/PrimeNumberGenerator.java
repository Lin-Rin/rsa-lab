package com.example.rsalab.util.math;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;
import io.github.kosssst.asymcryptolab1.generators.L20Generator;

@Component
public class PrimeNumberGenerator implements Supplier<BigInteger> {
    private final int length;
    private final L20Generator generator;

    public PrimeNumberGenerator(int length) {
        this.length = length;
        this.generator = new L20Generator();
    }

    @Override
    public BigInteger get() {
        Predicate<BigInteger> simplicityTest = new SimplicityTest();
        BigInteger number = new BigInteger(generator.generate(this.length), 2);
        BigInteger limit = BigInteger.TWO.pow(this.length).subtract(number).divide(BigInteger.TWO);
        if (number.mod(BigInteger.TWO).equals(BigInteger.ZERO)) number = number.add(BigInteger.ONE);
        while (true) {
            for (BigInteger i = BigInteger.ZERO; i.compareTo(limit) <= 0; i = i.add(BigInteger.ONE)) {
                BigInteger newNumber = number.add(i.multiply(BigInteger.TWO));
                if (simplicityTest.test(newNumber)) return newNumber;
            }
            number = new BigInteger(generator.generate(this.length), 2);
        }
    }
}
