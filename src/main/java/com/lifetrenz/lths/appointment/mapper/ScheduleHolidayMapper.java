package com.lifetrenz.lths.appointment.mapper;

import org.springframework.stereotype.Component;

import com.lifetrenz.lths.appointment.dto.ScheduleHolidayDTO;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.model.collection.ScheduleHoliday;
import com.lifetrenz.lths.appointment.model.value_object.CustomerTransactionBase;

@Component
public class ScheduleHolidayMapper {

	public ScheduleHoliday mapDtoToEntity(ScheduleHolidayDTO dto, TokenPayLoad tokenPayLoad) throws Exception {

		CustomerTransactionBase cd = new CustomerTransactionBase();

		cd.setCustomerId(tokenPayLoad.getCustomerId());
		cd.setCustomerBusinessId(tokenPayLoad.getCustomerBusinessId());
		cd.setSiteId(dto.getSiteId());

		return new ScheduleHoliday(dto.getId(), dto.getDescription(), dto.getHolidayName(), dto.getHolidayDate(),
				dto.getHolidaySiteId(), cd);

	}

	public ScheduleHolidayDTO mapEntityToDto(ScheduleHoliday dto) throws Exception {

		return new ScheduleHolidayDTO(dto.getId(), dto.getDescription(), dto.getHolidayName(), dto.getHolidayDate(),
				dto.getHolidaySiteId(), null, null, null);

	}
}
