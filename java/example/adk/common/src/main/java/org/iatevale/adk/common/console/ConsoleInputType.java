package org.iatevale.adk.common.console;

public sealed interface ConsoleInputType {
    record Prompt(String text) implements ConsoleInputType {}
    record Quit() implements ConsoleInputType {}
    record Empty() implements ConsoleInputType {}
}
