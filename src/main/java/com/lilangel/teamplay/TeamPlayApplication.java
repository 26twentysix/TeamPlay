package com.lilangel.teamplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeamPlayApplication {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
        SpringApplication.run(TeamPlayApplication.class, args);
    }

    public static boolean testMethod(boolean flag) {
        System.out.println("Starting app configuration.");
        if (flag) {
            System.out.println("Configuration done.");
            main(null);
            return true;
        }
        else {
            System.out.println("Something went wrong.");
            return false;
        }
    }
}
