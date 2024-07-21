package com.tanda.payments.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultParameterItem{

	@JsonProperty("Value")
	private int value;

	@JsonProperty("Key")
	private String key;
}