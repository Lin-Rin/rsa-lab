package com.example.rsalab.util.math.rabin;

import io.github.kosssst.asymcryptolab1.generators.L20Generator;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import com.example.rsalab.util.math.rsa.SimplicityTest;

@Component
public class NumberGenerator {

    // type: 4k+3,  test by millerRabin
    public BigInteger getPrimeNumber(Long keySize) {
        L20Generator generator = new L20Generator();
        BigInteger k = new BigInteger(generator.generate(keySize.intValue() - 3), 2);
        BigInteger FOUR = new BigInteger("4", 10);
        BigInteger THREE = new BigInteger("3", 10);
        while (true) {
            BigInteger candidate = k.multiply(FOUR).add(THREE);

            if (candidate.toString(2).length() > keySize) {
                candidate = candidate.mod(BigInteger.TWO.pow(keySize.intValue()).subtract(BigInteger.ONE));
            }

            if (new SimplicityTest().test(candidate)) {
                return candidate;
            }

            k = k.add(BigInteger.ONE);
        }
    }

    public BigInteger get64BitNumber() {
        return new BigInteger("1" + (new L20Generator().generate(63)), 2);
    }

    // result < n **(result.compareTo(n) < 0)**, **len_n==len_res**
    public BigInteger generateNumber(BigInteger n) {
        return new BigInteger((new L20Generator().generate(n.toString(2).length())), 2).mod(n);
    }

}
