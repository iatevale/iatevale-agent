package org.iatevale.adk.common.console;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Console implements AutoCloseable {

    final private String prompt;

    final private Scanner scanner;

    public Console(String hello, String prompt) {
        this.prompt = prompt;
        scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        System.out.println(hello);
    }

    @Override
    public void close() throws Exception {
        scanner.close();
    }

    public ConsoleInputType Input() {
        System.out.print(prompt);
        String userInput = scanner.nextLine();
        if ("quit".equalsIgnoreCase(userInput)) {
            return new ConsoleInputType.Quit();
        } else if (userInput.trim().isEmpty()) {
            return new ConsoleInputType.Empty();
        } else {
            return new ConsoleInputType.Prompt(userInput);
        }
    }

    public void output(String message) {
        System.out.println(message);
    }

}
