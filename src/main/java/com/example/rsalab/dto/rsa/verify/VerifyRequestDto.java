package com.example.rsalab.dto.rsa.verify;

import lombok.Data;

@Data
public class VerifyRequestDto {
    private String modulus;
    private String publicExponent;
    private String message;
    private String signature;
}
