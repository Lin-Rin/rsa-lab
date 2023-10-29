package com.example.rsalab.dto.rsa.encrypt;

import lombok.Data;

@Data
public class EncryptRequestDto {
    private String modulus;
    private String publicExponent;
    private String message;
}
