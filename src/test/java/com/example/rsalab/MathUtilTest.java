package com.example.rsalab;

import com.example.rsalab.util.math.rabin.MathUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigInteger;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MathUtilTest {
    @InjectMocks
    private MathUtil mathUtil;

    @Test
    void sqrtTest1() {
        BigInteger y = new BigInteger("67", 10);
        BigInteger p = new BigInteger("7", 10);
        BigInteger q = new BigInteger("11", 10);

        // 12 65 contain
        List<BigInteger> sqrt = mathUtil.sqrt(y, p, q);

        sqrt.forEach(num -> System.out.print(num.toString(10) + " "));
    }

    @Test
    void sqrtTest2() {
        BigInteger y = new BigInteger("15", 10);
        BigInteger p = new BigInteger("7", 10);
        BigInteger q = new BigInteger("11", 10);

        // 13 64
        List<BigInteger> sqrt = mathUtil.sqrt(y, p, q);

        sqrt.forEach(num -> System.out.print(num.toString(10) + " "));
    }


    @Test
    void extendedEuclideanTest1() {
        BigInteger a = new BigInteger("7", 10);
        BigInteger b = new BigInteger("11", 10);

        // 7 11 -> -3 2
        mathUtil.extendedEuclidean(a, b).forEach(num -> System.out.print(num + " "));
    }

    @Test
    void extendedEuclideanTest() {
        BigInteger a = new BigInteger("156", 10);
        BigInteger b = new BigInteger("389", 10);
        // 156 389 -> -192 77

        mathUtil.extendedEuclidean(a, b).forEach(num -> System.out.print(num + " "));
    }
}
