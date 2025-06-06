package com.google.adk.samples.agents.parallel.impl.phase2;

import com.google.adk.agents.LlmAgent;
import org.iatevale.adk.common.model.AgentConfig;

public class SynthesisFactory {

    static public LlmAgent instantiate() {
        return AgentConfig.apply(LlmAgent.builder())
                .name("SynthesisAgent")
                .instruction(
                        """
                              You are an AI Assistant responsible for combining research findings into a structured report.
                              Your primary task is to synthesize the following research summaries, clearly attributing findings to their source areas. Structure your response using headings for each topic. Ensure the report is coherent and integrates the key points smoothly.
                              **Crucially: Your entire response MUST be grounded *exclusively* on the information provided in the 'Input Summaries' below. Do NOT add any external knowledge, facts, or details not present in these specific summaries.**
                              **Input Summaries:**
       
                              *   **Renewable Energy:**
                                  {renewable_energy_result}
       
                              *   **Electric Vehicles:**
                                  {ev_technology_result}
       
                              *   **Carbon Capture:**
                                  {carbon_capture_result}
       
                              **Output Format:**
       
                              ## Summary of Recent Sustainable Technology Advancements
       
                              ### Renewable Energy Findings
                              (Based on RenewableEnergyResearcher's findings)
                              [Synthesize and elaborate *only* on the renewable energy input summary provided above.]
       
                              ### Electric Vehicle Findings
                              (Based on EVResearcher's findings)
                              [Synthesize and elaborate *only* on the EV input summary provided above.]
       
                              ### Carbon Capture Findings
                              (Based on CarbonCaptureResearcher's findings)
                              [Synthesize and elaborate *only* on the carbon capture input summary provided above.]
       
                              ### Overall Conclusion
                              [Provide a brief (1-2 sentence) concluding statement that connects *only* the findings presented above.]
       
                              Output *only* the structured report following this format. Do not include introductory or concluding phrases outside this structure, and strictly adhere to using only the provided input summary content.
                              """)
                .description("Combines research findings from parallel agents into a structured, cited report, strictly grounded on provided inputs.")
                // No tools needed for merging
                // No output_key needed here, as its direct response is the final output of the sequence
                .build();
    }

}
