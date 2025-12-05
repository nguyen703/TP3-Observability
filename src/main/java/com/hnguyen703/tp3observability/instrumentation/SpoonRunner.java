package com.hnguyen703.tp3observability.instrumentation;

import spoon.Launcher;

public class SpoonRunner {
    public static void main(String[] args) {
        Launcher spoon = new Launcher();

        spoon.addInputResource("src/main/java/com/hnguyen703/tp3observability/services/ProductService.java");

        spoon.setSourceOutputDirectory("src/main/java");

        spoon.addProcessor(new LoggingProcessor());

        spoon.run();

        System.out.println("Spoon instrumentation complete. Check ProductService.java!");
    }
}