package com.example.rsalab.service;

import com.example.rsalab.util.math.PrimeNumberGenerator;
import com.example.rsalab.util.math.SimplicityTest;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class GenerateKeysService {
    private final Supplier<Long> supplier = new PrimeNumberGenerator();
    private final Predicate<Long> predicate = new SimplicityTest();

    public List<Long> generateKeys() {
        List<Long> list = new ArrayList<>();

        for (;;) {
            list.clear();

            list.add(supplier.get());
            list.add(supplier.get());
            list.add(supplier.get());
            list.add(supplier.get());

            list = list.stream()
                    .filter(predicate)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());

            if (list.size() == 4) {
                Long p = list.get(0);
                Long q = list.get(1);
                Long p1 = list.get(2);
                Long q1 = list.get(3);
                if (p*q <= p1*q1) {
                    break;
                }
            }
        }

        return list;
    }
}
