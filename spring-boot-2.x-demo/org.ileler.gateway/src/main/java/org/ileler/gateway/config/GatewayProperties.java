/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.ileler.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Validated
@ConfigurationProperties("gateway")
public class GatewayProperties {

	/**
	 * List of StaticResource
	 */
	@NotNull
	@Valid
	private List<StaticResourcePatternDefinition> staticResource = new ArrayList<>();

	public List<StaticResourcePatternDefinition> getStaticResource() {
		return staticResource;
	}

	public void setStaticResource(List<StaticResourcePatternDefinition> staticResource) {
		this.staticResource = staticResource;
	}

	@Override
	public String toString() {
		return "GatewayProperties{" +
				"staticResource=" + staticResource +
				'}';
	}

}

@Validated
class StaticResourcePatternDefinition {

	@NotEmpty
	private String[] patterns;

	@NotEmpty
	private String[] locations;

	private String defaultPage;

	private String cacheControl;

	public String[] getPatterns() {
		return patterns;
	}

	public void setPatterns(String[] patterns) {
		this.patterns = patterns;
	}

	public String[] getLocations() {
		return locations;
	}

	public void setLocations(String[] locations) {
		this.locations = locations;
	}

	public String getDefaultPage() {
		return defaultPage;
	}

	public void setDefaultPage(String defaultPage) {
		this.defaultPage = defaultPage;
	}

	public String getCacheControl() {
		return cacheControl;
	}

	public void setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
	}

	public String toString() {
		return "StaticResourcePatternDefinition{patterns='" + this.patterns + '\'' + ", locations=" + this.locations + '}';
	}

}