package com.wilderman.reviewer.data.dto.admin;

import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.Review;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.db.primary.entities.enumtypes.PatientStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class AdminPatientOutput {
    private Long id;
    private String fname;
    private String lname;
    private String phone;
    private PatientStatus status;
    private Date lastVisitedOn;
    private boolean sendable = false;
    private Integer attempts;
    private List<AdminPatientVisitOutput> visits;

    public AdminPatientOutput(Patient patient) {
        id = patient.getId();
        fname = patient.getFname();
        lname = patient.getLname();
        phone = patient.getPhone();
        status = patient.getStatus();
        lastVisitedOn = patient.getVisits().stream().findFirst().get().getVisitedOn();
        sendable = PatientStatus.sendable().contains(patient.getStatus()) && patient.getAttempts() < 3;
        attempts = patient.getAttempts();

        if (patient.getVisits() != null) {
            visits = patient.getVisits().stream()
                    .map(visit -> new AdminPatientVisitOutput(visit)).collect(Collectors.toList());
        }

    }
}
