package org.iatevale.example.adk.common.console;

public class ConsoleLoop {

    public static void run(String hello, String prompt, ConsoleListener consoleListener) {
        try (Console console = new Console(hello, prompt)) {
            while (true) {
                final ConsoleInputType input = console.Input();
                if (input instanceof ConsoleInputType.Quit) {
                    break;
                } else if (input instanceof ConsoleInputType.Empty) {
                    continue;
                } else {
                    consoleListener.onInput(((ConsoleInputType.Prompt) input).text(), console::output);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
