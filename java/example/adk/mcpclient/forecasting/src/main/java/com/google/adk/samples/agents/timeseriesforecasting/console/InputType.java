package com.google.adk.samples.agents.timeseriesforecasting.console;

public sealed interface InputType {
    record Prompt(String text) implements InputType {}
    record Quit() implements InputType {}
    record Empty() implements InputType {}
}
