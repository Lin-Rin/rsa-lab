package com.example.rsalab.dto.rabin.verify;

import lombok.Data;

@Data
public class VerifyRequest {
    private String modulus;
    private String message;
    private String signature;
}
