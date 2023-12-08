package com.example.rsalab.dto.rabin.encrypt;

import lombok.Data;

@Data
public class EncryptResponse {
    private String ciphertext;
    private String parity;
    private String jacobiSymbol;
}
