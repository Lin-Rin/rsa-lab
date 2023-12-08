package com.example.rsalab.model;

import java.math.BigInteger;
import lombok.Data;

@Data
public class RabinPublicKey {
    private BigInteger n;
    private BigInteger b;
}
