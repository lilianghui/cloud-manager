package com.lilianghui.application.task;

import org.springframework.boot.CommandLineRunner;

public class HelloWorldCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello World!");
        System.out.println(args);
    }
}
