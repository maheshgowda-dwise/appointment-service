package com.lifetrenz.lths.appointment.search.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.common.app.exception.BadRequestException;
import com.lifetrenz.lths.appointment.common.app.exception.FailedException;
import com.lifetrenz.lths.appointment.common.app.exception.NotFoundException;
import com.lifetrenz.lths.appointment.common.enums.MessageEventStatus;
import com.lifetrenz.lths.appointment.common.enums.MessageRequestStatus;
import com.lifetrenz.lths.appointment.dto.CustomerTransactionAttributeDTO;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.mapper.EventsMapper;
import com.lifetrenz.lths.appointment.model.collection.ComponentFilter;
import com.lifetrenz.lths.appointment.model.events.MessageEvent;
import com.lifetrenz.lths.appointment.repository.command.IComponentFilterRepository;
import com.lifetrenz.lths.appointment.search.dto.ComponentFilterDto;
import com.lifetrenz.lths.appointment.search.mapper.ComponentFilterMapper;
import com.lifetrenz.lths.appointment.search.query.ComponentFilterRepository;
import com.lifetrenz.lths.appointment.search.service.ComponentFilterService;
import com.lifetrenz.lths.appointment.service.MessageEventService;


@Component
public class ComponentFilterServiceImpl implements ComponentFilterService {

	@Autowired
	ComponentFilterRepository componentFilterRepository;

	@Autowired
	ComponentFilterMapper componentFilterMapper;
	
	@Autowired
	IComponentFilterRepository iComponentFilterRepository;
	
	@Autowired
	EventsMapper eventsMapper;
	
	@Autowired
	MessageEventService messageEventService;

	@Override
	public ComponentFilterDto saveComponentFilter(ComponentFilterDto filter, TokenPayLoad tokenPayLoad, String path)
			throws ApplicationException {

		try {
			if (filter.getCustomerTransactionAttribute() == null) {
				throw new BadRequestException("Transaction attribute cannot be null!");
			}

			ComponentFilter componentFilter = this.iComponentFilterRepository
					.save(this.componentFilterMapper.mapComponentFilterDtoToEntity(filter));
			filter.setId(componentFilter.getId());
		} catch (Exception e) {
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE,e.getMessage());

			this.messageEventService.saveEvent(testEvent);
		}
		return filter;

	}

	@Override
	public List<ComponentFilterDto> getComponentFilter(String type, String level, Long userId, Long siteId,
			Long customerBusinessId, String path) throws ApplicationException {

		List<ComponentFilterDto> result = new ArrayList<>();
		try {
			result = this.componentFilterRepository.getComponentFilter(type, level, userId, siteId, customerBusinessId);
		} catch (Exception e) {
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE,e.getMessage());

			this.messageEventService.saveEvent(testEvent);
		}

		return result;
	}

	@Override
	public ComponentFilterDto updateDefaultFilter(ComponentFilterDto filter, String path) throws ApplicationException {
		try {
			List<ComponentFilter> result = new ArrayList<>();
			ComponentFilter res = this.iComponentFilterRepository.findById(filter.getId()).get();
			if (res == null) {
				throw new FailedException("Filter not found!");
			} else {
				res.setIsDefault(filter.getIsDefault());
				result.add(res);
				if (filter.getIsDefault()) {
					List<ComponentFilter> ress = this.iComponentFilterRepository
							.findByComponentTypeAndFilterUserIdAndIsDefaultAndCustomerTransactionAttribute_CustomerBusinessId(
									res.getComponentType(), res.getFilterUserId(), true,
									filter.getCustomerTransactionAttribute().getCustomerBusinessId());
					if (ress != null && ress.size() > 0) {
						for (ComponentFilter componentFilter : ress) {
							if (componentFilter.getId() != filter.getId()) {
								componentFilter.setIsDefault(false);
								result.add(componentFilter);
							}

						}
					}

				}

				this.iComponentFilterRepository.saveAll(result);

			}

			return res == null ? null : this.componentFilterMapper.mapComponentFilterEntityToDto(res);
		} catch (Exception e) {
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE,e.getMessage());

			this.messageEventService.saveEvent(testEvent);
			throw new FailedException("Failed to Update Default filter");
		}
	}

	@Override
	public ComponentFilterDto deleteFilter(String id) throws ApplicationException {
		try {
			ComponentFilter res = this.iComponentFilterRepository.findById(id).get();
			if (res == null) {
				throw new NotFoundException("Filter not found!");
			} else {
				res.getCustomerTransactionAttribute().setActive(false);
				res = this.iComponentFilterRepository.save(res);

			}

			return res == null ? null : this.componentFilterMapper.mapComponentFilterEntityToDto(res);
		} catch (Exception e) {
			throw new FailedException("Failed to delete the filter");
		}
		
	}

	@Override
	public ComponentFilterDto updateComponentFilter(ComponentFilterDto filter, String path) throws ApplicationException {
		try {
			ComponentFilter res = this.iComponentFilterRepository.findById(filter.getId()).get();
			if (res == null) {
				throw new NotFoundException("Filter not found!");
			} else {
				res.setFilterName(filter.getFilterName());

				this.iComponentFilterRepository.save(res);

			}

			return this.componentFilterMapper.mapComponentFilterEntityToDto(res);
		} catch (Exception e) {
			MessageEvent testEvent = this.eventsMapper.convertToMessageEvent(null, null, 1, null,
					MessageRequestStatus.PENDING, path + "," + this.getClass().getSimpleName(),
					MessageEventStatus.FAILED_ON_CODE,e.getMessage());

			this.messageEventService.saveEvent(testEvent);
			throw new FailedException("Failed to Update Filter");
		}
	}

}
