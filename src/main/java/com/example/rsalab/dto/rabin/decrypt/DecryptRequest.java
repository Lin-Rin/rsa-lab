package com.example.rsalab.dto.rabin.decrypt;

import lombok.Data;

@Data
public class DecryptRequest {
    private String ciphertext;
    private String parity;
    private String jacobiSymbol;
}
