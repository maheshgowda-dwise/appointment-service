package com.lifetrenz.lths.appointment.model.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lifetrenz.lths.appointment.enums.ReferralType;
import com.lifetrenz.lths.appointment.model.enums.FollowupRequestStatus;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "followup_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowupRequest {

	@Id
	private String id;

	private String userName;

	private Long userId;

	private String userSpecialisation;

	private String patientName;

	private Long patientId;
	
	private String specialisationIdentifier;

	private String mpi;

	private Long preferredDate;

	private String followupNote;

	private FollowupRequestStatus status;

	private CustomerTransactionBase customerTrasaction;

	private Long encounterId;
	
	private String appointmentId;

	@NotNull
	private String eventIdentifier;

	private String referralId;

	private ReferralType referralTypes;

	private String referralSiteName;

	private String referralReason;

	private String priority;
	
	private Long genderId;

	private String gender;

	private String dateOfBirth;
	
	private String referredDocname;
	
	private Long referredDocId;
	
	private String referredDocSpecialisation;
	
	private String referredSpecialisationIdentifier;

}
