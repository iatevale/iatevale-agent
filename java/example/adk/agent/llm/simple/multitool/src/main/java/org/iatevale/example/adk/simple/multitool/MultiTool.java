package org.iatevale.example.adk.simple.multitool;

import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import org.iatevale.example.adk.common.console.ConsoleLoop;
import org.iatevale.example.adk.common.logger.AgentLogger;
import org.iatevale.example.adk.simple.multitool.agent.AgentFactory;
import org.iatevale.example.adk.simple.multitool.runner.MultiToolRunner;
import org.iatevale.example.adk.simple.multitool.tool.CurrentTimeToolFactory;
import org.iatevale.example.adk.simple.multitool.tool.WeatherToolFactory;

import java.util.function.Consumer;
import java.util.logging.Level;

public class MultiTool {

    final private static String USER_ID = "student";

    public static void main(String[] args) {

        AgentLogger.setLevel(Level.WARNING);

        // Se ensambla el agente
        final CurrentTimeToolFactory currentTimeToolFactory = CurrentTimeToolFactory.instantiate();
        final WeatherToolFactory weatherToolFactory = WeatherToolFactory.instantiate();
        final AgentFactory agentFactory = AgentFactory.instantiate(currentTimeToolFactory.functionTool(), weatherToolFactory.functionTool());

        // Se crea en runner el agente, con las herramientas cargadas
        final InMemoryRunner runner = new InMemoryRunner(agentFactory.llmAgent());

        // Se crea una sesión temporal para el agente
        final Session session = runner.sessionService()
                .createSession(runner.appName(), USER_ID)
                .blockingGet();

        // Se crea el agente
        final MultiToolRunner multiToolRunner = new MultiToolRunner(runner, session);
        final MultiTool multiTool = new MultiTool(multiToolRunner);

        // Consola para interaccion con el usuario
        ConsoleLoop.run(Constants.HELLO, Constants.PROMPT, multiTool::onInput);

    }

    final MultiToolRunner multiToolRunner;

    public MultiTool(MultiToolRunner multiToolRunner) {
        this.multiToolRunner = multiToolRunner;
    }

    void onInput(String prompt, Consumer<String> consoleOutput) {
        multiToolRunner.execute(prompt, consoleOutput);
    }

}