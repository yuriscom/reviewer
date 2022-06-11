package com.wilderman.reviewer.data.dto;

import com.wilderman.reviewer.db.primary.entities.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatientOutput {
    private String fullname;

    public PatientOutput(Patient patient) {
        this.fullname = patient.getFullname();
    }
}