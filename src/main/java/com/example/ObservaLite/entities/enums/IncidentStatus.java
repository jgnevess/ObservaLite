package com.example.ObservaLite.entities.enums;

public enum IncidentStatus {
	OPEN("Open"),
	ACKNOWLEDGED("Acknowledged"),
	RESOLVED("Resolved");
	
	private String status;
	
	IncidentStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return this.status;
	}
}
