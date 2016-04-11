package org.transandalus.backend.web.rest.dto;

public class RecaptchaDTO {

	private String widgetId;

	private String response;

	private String remoteAddress;
	
	public String getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	
	@Override
	public String toString() {
		return "RecaptchaDTO{widgetId='" + widgetId + "', response='" + response
				+ "'}";
	}
}
