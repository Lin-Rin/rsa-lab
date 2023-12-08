package com.example.rsalab.dto.rabin.znp;

import lombok.Data;

import java.util.List;

@Data
public class CandidatesResponse {
    private List<String> candidates;
    private List<String> t;
}
