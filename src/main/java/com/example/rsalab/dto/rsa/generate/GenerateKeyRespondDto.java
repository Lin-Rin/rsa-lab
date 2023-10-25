package com.example.rsalab.dto.rsa.generate;

import lombok.Data;

@Data
public class GenerateKeyRespondDto {
    private String modulus;
    private String publicExponent;
}
