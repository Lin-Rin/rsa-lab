package com.example.rsalab.model;

import lombok.Data;
import java.math.BigInteger;

@Data
public class PrivateKey {
    private BigInteger p;
    private BigInteger q;
    private BigInteger d;
}
