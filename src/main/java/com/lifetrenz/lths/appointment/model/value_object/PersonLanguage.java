package com.lifetrenz.lths.appointment.model.value_object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonLanguage {
	private Long id;

	private Person person;

	private Language language;

	private Boolean preferedLanguage;

	private Boolean motherTongue;

	private Boolean canSpeak;

	private Boolean canRead;

	private Boolean canWrite;


}
