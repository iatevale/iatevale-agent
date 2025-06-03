package org.iatevale.adk.common.console;

public class ConsoleLoop {

    public static void run(String hello, String prompt, ConsoleListener consoleListener) {
        try (Console console = new Console(hello, prompt)) {
            while (true) {
                final InputType input = console.Input();
                if (input instanceof InputType.Quit) {
                    break;
                } else if (input instanceof InputType.Empty) {
                    continue;
                } else {
                    consoleListener.onInput(((InputType.Prompt) input).text(), console::output);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
