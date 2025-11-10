package com.lifetrenz.lths.appointment.model.value_object;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityDocument {

	private Long id;

	private Patient patient;

	private IdentityDocumentType identityDocumentType;

	private Date proofIssueDate;

	private Date proofExpiryDate;

	private String refferenceNumber;

}
