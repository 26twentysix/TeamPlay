package com.lilangel.TeamPlay;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
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
