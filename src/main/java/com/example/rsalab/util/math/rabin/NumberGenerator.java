package com.example.rsalab.util.math.rabin;

import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class NumberGenerator {

    // type: 4k+1,  test by millerRabin
    public BigInteger getPrimeNumber(Long keySize) {
        throw new UnsupportedOperationException();
    }

    public BigInteger get64BitNumber() {
        throw new UnsupportedOperationException();
    }

    // result < n **(result.compareTo(n) < 0)**, **len_n==len_res**
    public BigInteger generateNumber(BigInteger n) {
        throw new UnsupportedOperationException();
    }

}
