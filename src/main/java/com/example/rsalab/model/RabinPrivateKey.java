package com.example.rsalab.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class RabinPrivateKey {
    private BigInteger p;
    private BigInteger q;
}
