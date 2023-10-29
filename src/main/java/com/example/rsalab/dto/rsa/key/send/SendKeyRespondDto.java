package com.example.rsalab.dto.rsa.key.send;

import lombok.Data;

@Data
public class SendKeyRespondDto {
    private String key;
    private String signature;
}
