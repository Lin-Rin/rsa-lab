package com.example.rsalab.dto.rabin.znp;

import lombok.Data;

@Data
public class FindPQRequest {
    private String modulus;
    private String root;
    private String t;
}
