package com.example.rsalab.dto.rabin.generate;

import lombok.Data;

@Data
public class ServerKeyResponse {
    private String modulus;
    private String b;
}
