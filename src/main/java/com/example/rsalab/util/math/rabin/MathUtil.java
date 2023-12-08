package com.example.rsalab.util.math.rabin;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class MathUtil {

    public BigInteger gcd(BigInteger a, BigInteger b) {
        BigInteger d = BigInteger.ONE;

        while (isPair(a) && isPair(b)) {
            a = a.divide(BigInteger.TWO);
            b = b.divide(BigInteger.TWO);
            d = d.multiply(BigInteger.TWO);
        }

        while (isPair(a)) {
            a = a.divide(BigInteger.TWO);
        }

        while (!b.equals(BigInteger.ZERO)) {
            while (isPair(b)) {
                b = b.divide(BigInteger.TWO);
            }
            BigInteger temp = a.subtract(b).abs();
            a = a.min(b);
            b = temp;
        }

        d = d.multiply(a);
        return d;
    }

    public List<BigInteger> extendedEuclidean(BigInteger p, BigInteger q) {
        List<BigInteger> result = new ArrayList<>();

        if (q.equals(BigInteger.ZERO)) {
            result.add(p);
            result.add(BigInteger.ONE);
            result.add((BigInteger.ZERO));
        } else {
            List<BigInteger> newResult = extendedEuclidean(q, p.mod(q));
            result.add(newResult.get(0));
            result.add(newResult.get(2));
            result.add(newResult.get(1).subtract(p.divide(q).multiply(newResult.get(2))));
        }

        return result;
    }

    public List<BigInteger> sqrt(BigInteger y, BigInteger p, BigInteger q) {
        BigInteger s1 = y.modPow(p.add(BigInteger.ONE).divide(new BigInteger("4", 10)), p);
        BigInteger s2 = y.modPow(q.add(BigInteger.ONE).divide(new BigInteger("4", 10)), q);

        List<BigInteger> uv = extendedEuclidean(p, q);

        List<BigInteger> x = new ArrayList<>();

        BigInteger u = uv.get(1);
        BigInteger v = uv.get(2);

        x.add(u.multiply(p).multiply(s2).add(v.multiply(q).multiply(s1)).mod(p.multiply(q)));
        x.add(u.multiply(p).multiply(s2).subtract(v.multiply(q).multiply(s1)).mod(p.multiply(q)));
        x.add(BigInteger.ZERO.subtract(u.multiply(p).multiply(s2)).add(v.multiply(q).multiply(s1)).mod(p.multiply(q)));
        x.add(BigInteger.ZERO.subtract(u.multiply(p).multiply(s2)).subtract(v.multiply(q).multiply(s1)).mod(p.multiply(q)));

        return x;
    }

    public BigInteger getJacobiSymbol(BigInteger a, BigInteger n) {
        int result = 1;
        BigInteger THREE = new BigInteger("3", 10);
        BigInteger FOUR = new BigInteger("4", 10);
        BigInteger FIVE = new BigInteger("5", 10);
        BigInteger EIGHT = new BigInteger("8", 10);


        while (!a.equals(BigInteger.ZERO)) {
            while (a.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                a = a.divide(BigInteger.TWO);
                BigInteger r = n.mod(EIGHT);
                if (r.equals(THREE) || r.equals(FIVE)) {
                    result = -result;
                }
            }

            BigInteger temp = a;
            a = n;
            n = temp;

            if (a.mod(FOUR).equals(THREE) && n.mod(FOUR).equals(THREE)) {
                result = -result;
            }

            a = a.mod(n);
        }

        if (n.equals(BigInteger.ONE)) {
            return BigInteger.valueOf(result);
        } else {
            return BigInteger.ZERO;
        }
    }

    private boolean isPair(BigInteger a) {
        return a.mod(BigInteger.TWO).equals(BigInteger.ZERO);
    }
}
