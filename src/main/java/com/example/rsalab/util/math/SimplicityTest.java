package com.example.rsalab.util.math;

import org.springframework.stereotype.Component;
import java.util.function.Predicate;

@Component
public class SimplicityTest implements Predicate<Long> {
    @Override
    public boolean test(Long aLong) {
        return false;
    }
}
