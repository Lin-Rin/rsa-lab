package com.example.rsalab.dto.rsa.key.send;

import lombok.Data;

@Data
public class SendKeyRequestDto {
    private String modulus;
    private String publicExponent;
}
