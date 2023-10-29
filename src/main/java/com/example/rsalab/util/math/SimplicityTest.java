package com.example.rsalab.util.math;

import io.github.kosssst.asymcryptolab1.generators.L20Generator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Predicate;

@Component
public class SimplicityTest implements Predicate<BigInteger> {
    @Override
    public boolean test(BigInteger number) {
        BigInteger pMinusOne = number.subtract(BigInteger.ONE);
        BigInteger s = new BigInteger("0", 10);
        BigInteger d = new BigInteger("0", 10);
        L20Generator generator = new L20Generator();

        for (BigInteger i = log2(pMinusOne); i.compareTo(BigInteger.ZERO) >= 0; i = i.subtract(BigInteger.ONE)) {
            BigInteger TwoPowS = BigInteger.TWO.pow(i.intValue());
            if (pMinusOne.mod(TwoPowS).equals(BigInteger.ZERO)) {
                d = pMinusOne.divide(TwoPowS);
                s = i;
                break;
            }
        }

        for (int k = 0; k < 2048; k++) {
            BigInteger x = new BigInteger(generator.generate(number.bitLength()), 2);
            x = x.mod(number);

            if (x.gcd(number).compareTo(BigInteger.ONE) > 0) return false;

            if (Math.abs(x.modPow(d, number).compareTo(BigInteger.ZERO)) == 1) continue;

            boolean prime = false;
            for (BigInteger i = BigInteger.ONE; i.compareTo(s) < 0; i = i.add(BigInteger.ONE)) {
                BigInteger xr = x.modPow(d.multiply(BigInteger.TWO.pow(i.intValue())), number);
                if (xr.compareTo(BigInteger.ZERO) == -1) {
                    prime = true;
                    break;
                }
                if (xr.compareTo(BigInteger.ZERO) == 1) return false;
            }

            if (!prime) return false;
        }

        return true;
    }

    private BigInteger log2(BigInteger number) {
        int precision = 100;
        MathContext mathContext = new MathContext(precision, RoundingMode.HALF_UP);
        BigDecimal result = BigDecimal.valueOf(Math.log(number.doubleValue()) / Math.log(2));
        result = result.round(mathContext);
        return result.toBigInteger();
    }
}
