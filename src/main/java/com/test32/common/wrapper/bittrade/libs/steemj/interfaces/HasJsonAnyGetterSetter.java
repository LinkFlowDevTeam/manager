package com.test32.common.wrapper.bittrade.libs.steemj.interfaces;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.Map;

public interface HasJsonAnyGetterSetter {
	@JsonAnyGetter
	Map<String, Object> _getter();
	@JsonAnySetter
	void _setter(String key, Object value);
}
