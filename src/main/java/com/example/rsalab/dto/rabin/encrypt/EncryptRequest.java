package com.example.rsalab.dto.rabin.encrypt;

import lombok.Data;

@Data
public class EncryptRequest {
    private String modulus;
    private String b;
    private String text;
}
