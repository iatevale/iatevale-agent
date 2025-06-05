package org.iatevale.adk.common.model;

import com.google.adk.agents.LlmAgent;
import com.google.adk.models.Gemini;
import org.iatevale.config.AdkParameters;
import org.iatevale.config.IATevaleConfig;

public class AgentConfig {

    static public LlmAgent.Builder apply(LlmAgent.Builder builder) {
        final AdkParameters adkParameters = IATevaleConfig.getAdkParameters();
        final Gemini gemini = Gemini.builder()
                .modelName(adkParameters.modelName())
                .apiKey(adkParameters.apiKey())
                .build();
        return builder.model(gemini)
                .globalInstruction("Answer always in Spanish");
    }

}
