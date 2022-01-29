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
    private List<Review> reviews;

    public AdminPatientOutput(Patient patient) {
        id = patient.getId();
        fname = patient.getFname();
        lname = patient.getLname();
        phone = patient.getPhone();
        status = patient.getStatus();
        lastVisitedOn = patient.getVisits().stream().findFirst().get().getVisitedOn();

    }
}
