package org.iatevale.adk.common.console;

import java.util.function.Consumer;

@FunctionalInterface
public interface ConsoleListener {
    void onInput(String prompt, Consumer<String> consoleOutput);
}
