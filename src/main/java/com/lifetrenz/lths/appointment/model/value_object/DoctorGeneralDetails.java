package com.lifetrenz.lths.appointment.model.value_object;

import java.io.Serializable;
import java.util.List;

import com.lifetrenz.lths.appointment.dto.NewTelecomDTO;
import com.lifetrenz.lths.appointment.dto.SystemMasterDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorGeneralDetails implements Serializable {

	private static final long serialVersionUID = 5738828718173929132L;

	private List<SystemMasterDTO> specialties;

	private DoctorRoleDetails role;

	private String emailId;

	private NewTelecomDTO telecom;

}
