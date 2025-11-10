package com.lifetrenz.lths.appointment.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lifetrenz.lths.appointment.common.app.ApplicationResponse;
import com.lifetrenz.lths.appointment.common.app.constant.CommonConstants;
import com.lifetrenz.lths.appointment.common.app.exception.ApplicationException;
import com.lifetrenz.lths.appointment.dto.TokenPayLoad;
import com.lifetrenz.lths.appointment.search.dto.ComponentFilterDto;
import com.lifetrenz.lths.appointment.search.service.ComponentFilterService;
import com.lifetrenz.lths.appointment.service.TokenService;


@RestController
public class ComponentFilterController {

	@Autowired
	ComponentFilterService componentFilterService;

	@Autowired
	TokenService tokenService;

	@PostMapping("opd/component/filter")
	public ApplicationResponse<?> saveComponentFilter(@RequestHeader HttpHeaders headers,
			@RequestBody ComponentFilterDto filter) throws Exception {

		TokenPayLoad tokenPayLoad = null;
		tokenPayLoad = tokenService.getTokenPayload(headers);

		ComponentFilterDto res = null;
		try {
			res = this.componentFilterService.saveComponentFilter(filter, tokenPayLoad, this.getClass().getSimpleName());

			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, res);
		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@GetMapping("opd/component/filter")
	public ApplicationResponse<?> getComponentFilter(@RequestHeader HttpHeaders headers,
			@RequestParam(name = "type", required = true) String type,
			@RequestParam(name = "level", required = true) String level,
			@RequestParam(name = "userId", required = true) Long userId,
			@RequestParam(name = "siteId", required = true) Long siteId,
			@RequestParam(name = "customerBusinessId", required = true) Long customerBusinessId)
			throws ApplicationException {

		List<ComponentFilterDto> res = null;
		try {
			res = this.componentFilterService.getComponentFilter(type, level, userId, siteId, customerBusinessId, this.getClass().getSimpleName());

			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, res);
		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@PutMapping("opd/component/filter/default")
	public ApplicationResponse<?> updateDefaultFilter(@RequestHeader HttpHeaders headers,
			@RequestBody ComponentFilterDto filter) throws ApplicationException {

		ComponentFilterDto res = null;
		try {
			res = this.componentFilterService.updateDefaultFilter(filter, this.getClass().getSimpleName());

			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, res);
		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@PutMapping("opd/component/filter/update")
	public ApplicationResponse<?> updateComponentFilter(@RequestHeader HttpHeaders headers,
			@RequestBody ComponentFilterDto filter) throws ApplicationException {

		ComponentFilterDto res = null;
		try {
			res = this.componentFilterService.updateComponentFilter(filter, this.getClass().getSimpleName());

			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, res);
		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

	@DeleteMapping("opd/component/filter/{id}")
	public ApplicationResponse<?> deleteFilter(@RequestHeader HttpHeaders headers, @PathVariable String id)
			throws ApplicationException {

		ComponentFilterDto res = null;
		try {
			res = this.componentFilterService.deleteFilter(id);

			return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
					CommonConstants.OK, res);
		} catch (ApplicationException e) {
			return new ApplicationResponse<>(CommonConstants.ERROR,
					String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
		}
	}

}
