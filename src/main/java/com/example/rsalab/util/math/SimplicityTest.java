package com.example.rsalab.util.math;

import io.github.kosssst.asymcryptolab1.generators.L20Generator;
import org.springframework.stereotype.Component;
import java.math.BigInteger;
import java.util.function.Predicate;

@Component
public class SimplicityTest implements Predicate<BigInteger> {
    @Override
    public boolean test(BigInteger number) {
        L20Generator generator = new L20Generator();
        BigInteger s = new BigInteger("0", 10);
        BigInteger d = number.subtract(BigInteger.ONE);

        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            s = s.add(BigInteger.ONE);
            d = d.divide(BigInteger.TWO);
        }

        for (int k = 0; k < number.bitLength(); k++) {
            BigInteger a = new BigInteger(generator.generate(number.bitLength()), 2).mod(number);
            BigInteger x = a.modPow(d, number);

            if (x.equals(BigInteger.ONE) || x.equals(number.subtract(BigInteger.ONE))) continue;

            int r = 0;
            for (; s.compareTo(BigInteger.ZERO) > r; r++) {
                x = x.modPow(BigInteger.TWO, number);
                if (x.equals(BigInteger.ONE)) return false;
                if (x.equals(number.subtract(BigInteger.ONE))) break;
            }

            if (s.compareTo(BigInteger.ZERO) == r) return false;
        }

        return true;
    }
}
