package com.lifetrenz.lths.appointment.model.value_object;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lifetrenz.lths.appointment.util.AppUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Name implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@NotBlank
	public String firstname;

	public String middlename;

	@NotNull
	@NotBlank
	public String lastname;

	public String getFullName() {
		return (firstname + " " + (AppUtil.isNullString(middlename) ? "" : middlename + " ")
				+ (AppUtil.isNullString(lastname) ? "" : lastname));
	}

}
