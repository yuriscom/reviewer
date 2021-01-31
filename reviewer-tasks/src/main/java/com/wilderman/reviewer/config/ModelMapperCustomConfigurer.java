package com.wilderman.reviewer.config;

import com.github.jmnarloch.spring.boot.modelmapper.ModelMapperConfigurer;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.task.VisitorsReportIngestion.PatientVisitRecord;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperCustomConfigurer implements ModelMapperConfigurer {

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        TypeMap<PatientVisitRecord, Patient> patientTypeMap = modelMapper.createTypeMap(PatientVisitRecord.class, Patient.class);
        patientTypeMap.setPropertyCondition(Conditions.isNotNull());
//        patientTypeMap.addMapping(PatientVisitRecord::getPhone, Patient.standardizePhone);

        TypeMap<PatientVisitRecord, Visit> visitTypeMap = modelMapper.createTypeMap(PatientVisitRecord.class, Visit.class);
        visitTypeMap.setPropertyCondition(Conditions.isNotNull());

    }
}
