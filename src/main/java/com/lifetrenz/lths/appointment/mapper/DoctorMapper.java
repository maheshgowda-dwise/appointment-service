package com.lifetrenz.lths.appointment.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lifetrenz.lths.appointment.dto.OnlineDoctors;
import com.lifetrenz.lths.appointment.dto.Qualification;
import com.lifetrenz.lths.appointment.dto.QualificationDTO;
import com.lifetrenz.lths.appointment.dto.Registration;
import com.lifetrenz.lths.appointment.dto.Specialization;
import com.lifetrenz.lths.appointment.model.collection.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DoctorMapper {
	
	private static final Logger logger = LoggerFactory.getLogger(DoctorMapper.class);

	public OnlineDoctors mapToOnlineDoctors(User user) {
		try {
			String[] knownLanguage = user.getKnownLanguage() != null
					? user.getKnownLanguage().stream().map(lang -> lang.getName()).toArray(String[]::new)
					: null;

			Collection<Qualification> qualifications = mapQualifications(user.getUserQualification());

			return new OnlineDoctors(String.valueOf(user.getCoreUserId()), user.getName(),
					new Registration(user.getMedicalCouniclNo(), null, 0),
					user.getGender() != null ? user.getGender().getName() : null,
					user.getSpecialties() != null && !user.getSpecialties().isEmpty()
							? new Specialization(String.valueOf(user.getSpecialties().get(0).getId()), null, null)
							: null,
					user.getWorkExperience() != null && !user.getWorkExperience().isEmpty()
							? user.getWorkExperience().get(0).getFromDate().toString()
							: null,
					user.getProfessionalStatement(), qualifications, null, knownLanguage);
		} catch (Exception e) {
			logger.error("Failed to map User to OnlineDoctors: {}", e.getMessage(), e);
			return null;
		}
	}

	private Collection<Qualification> mapQualifications(List<QualificationDTO> qualificationDTOs) {
		if (qualificationDTOs == null || qualificationDTOs.isEmpty()) {
			return new ArrayList<>();
		}

		Qualification qualification = new Qualification(null, qualificationDTOs.get(0).getEducationLevel(), null,
				String.valueOf(qualificationDTOs.get(0).getDate()), null);

		return List.of(qualification);
	}

}
