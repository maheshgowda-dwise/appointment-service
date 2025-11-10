package com.lifetrenz.lths.appointment.model.value_object;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

	private Long id;

	private Boolean active;

	private String name;

	private String iso3;

	private String iso2;

	private String numericCode;

	private String phoneCode;

	private String capital;

	private String currency;

	private String currencySymbol;

	private String tid;

	private String nativeName;

	private String region;

	private String subregion;

	private String timezones;

	private Float latitude;

	private Float longitude;

	private String emoji;

	private String emojiU;

	private String nationality;

	private Set<State> states;

	private Set<City> cities;

}
