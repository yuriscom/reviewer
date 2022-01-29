package com.wilderman.reviewer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wilderman.reviewer.enums.Step;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StepData {
    private Step step;
    private String forwardToUrl;

    public StepData(Step step) {
        this.step = step;
    }
}