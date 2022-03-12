package com.wilderman.reviewer.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wilderman.reviewer.dto.StepData;
import com.wilderman.reviewer.enums.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StepOutput extends StepData {
    private Step step;
    ClientOutput clientDetails;
    private String forwardToUrl;

    public StepOutput(StepData stepData) {
        this.step = stepData.getStep();
        this.forwardToUrl = stepData.getForwardToUrl();
        this.clientDetails = new ClientOutput(stepData.getClient());
    }
}
