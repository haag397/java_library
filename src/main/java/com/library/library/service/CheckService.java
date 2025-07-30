package com.library.library.service;

import org.springframework.stereotype.Service;

@Service
public class CheckService {

    public String performCheck(String valueToCheck) throws InterruptedException {
        System.out.println("🔍 Checking value: " + valueToCheck);

        if ("slow".equalsIgnoreCase(valueToCheck)) {
            Thread.sleep(11000); // simulate timeout
            return "slow";
        } else {
            Thread.sleep(2000); // fast enough
            return "fast";
        }
    }
}