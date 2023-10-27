package com.example.rsalab.dto.rsa.key.recevie;

import lombok.Data;

@Data
public class RecevieKeyRequestDto {
    private String key;
    private String signature;
    private String modulus;
    private String publicExponent;
}
