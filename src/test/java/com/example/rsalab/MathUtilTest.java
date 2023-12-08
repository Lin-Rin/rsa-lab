package com.example.rsalab;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void jacobiTest1() {
        assertEquals(BigInteger.ONE.negate(), mathUtil.getJacobiSymbol(BigInteger.valueOf(12),   BigInteger.valueOf(7)));
        assertEquals(BigInteger.ONE.negate(), mathUtil.getJacobiSymbol(BigInteger.valueOf(12),   BigInteger.valueOf(77)));
        assertEquals(BigInteger.ONE,          mathUtil.getJacobiSymbol(BigInteger.valueOf(13),   BigInteger.valueOf(77)));
        assertEquals(BigInteger.ZERO,         mathUtil.getJacobiSymbol(BigInteger.valueOf(154),  BigInteger.valueOf(77)));

        assertEquals(BigInteger.ONE,          mathUtil.getJacobiSymbol(BigInteger.valueOf(23247669130L),  BigInteger.valueOf(1669)));
    }

    @Test
    void jacobiTest2() {
        assertEquals(BigInteger.ONE.negate(), mathUtil.getJacobiSymbol(BigInteger.valueOf(3),  BigInteger.valueOf(17)));
        assertEquals(BigInteger.ONE.negate(), mathUtil.getJacobiSymbol(BigInteger.valueOf(10), BigInteger.valueOf(21)));
        assertEquals(BigInteger.ZERO,         mathUtil.getJacobiSymbol(BigInteger.valueOf(7),  BigInteger.valueOf(35)));

        assertEquals(BigInteger.ONE,          mathUtil.getJacobiSymbol(BigInteger.valueOf(2),  BigInteger.valueOf(15)));
        assertEquals(BigInteger.ONE.negate(), mathUtil.getJacobiSymbol(BigInteger.valueOf(19), BigInteger.valueOf(35)));
        assertEquals(BigInteger.ONE,          mathUtil.getJacobiSymbol(BigInteger.valueOf(13), BigInteger.valueOf(51)));

        assertEquals(BigInteger.ONE.negate(), mathUtil.getJacobiSymbol(BigInteger.valueOf(3),  BigInteger.valueOf(17)));
        assertEquals(BigInteger.ONE.negate(), mathUtil.getJacobiSymbol(BigInteger.valueOf(31), BigInteger.valueOf(77)));
        assertEquals(BigInteger.ONE,          mathUtil.getJacobiSymbol(BigInteger.valueOf(5),  BigInteger.valueOf(91)));

    }

    @Test
    void jacobiTest3() {
        assertEquals(BigInteger.ONE.negate(), mathUtil.getJacobiSymbol(BigInteger.valueOf(122),  BigInteger.valueOf(143)));
        assertEquals(BigInteger.ZERO,         mathUtil.getJacobiSymbol(BigInteger.valueOf(169),  BigInteger.valueOf(143)));
        assertEquals(BigInteger.ONE,          mathUtil.getJacobiSymbol(BigInteger.valueOf(1669), BigInteger.valueOf(143)));
    }

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
