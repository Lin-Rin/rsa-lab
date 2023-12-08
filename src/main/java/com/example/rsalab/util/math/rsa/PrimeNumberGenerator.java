package com.example.rsalab.util.math.rsa;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;
import io.github.kosssst.asymcryptolab1.generators.L20Generator;

@Component
public class PrimeNumberGenerator implements Supplier<BigInteger> {
    @Setter
    private int length = 256;
    private final L20Generator generator;

    public PrimeNumberGenerator() {
        this.generator = new L20Generator();
    }

    @Override
    public BigInteger get() {
        Predicate<BigInteger> simplicityTest = new SimplicityTest();
        BigInteger number = new BigInteger("1" + generator.generate(this.length - 1), 2);
        if (number.mod(BigInteger.TWO).equals(BigInteger.ZERO)) number = number.add(BigInteger.ONE);

        BigInteger limit = BigInteger.TWO.pow(this.length).subtract(number).divide(BigInteger.TWO);

        while (true) {
            for (BigInteger i = BigInteger.ZERO; i.compareTo(limit) <= 0; i = i.add(BigInteger.ONE)) {
                BigInteger newNumber = number.add(i.multiply(BigInteger.TWO));
                if (simplicityTest.test(newNumber)) return newNumber;
            }
            number = new BigInteger("1" + generator.generate(this.length - 1), 2);
        }
    }
}
