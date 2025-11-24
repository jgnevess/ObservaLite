package com.example.ObservaLite.entities.enums;

public enum IncidentType {
	DOWNTIME("Downtime"), 
	ERROR_SPIKE("Error spike"),
	SSL_EXPIRING("SSL Expiring"),
	DNS_CHANGE("DNS Change"),
    PROJECT_DOWN("Project down");
	
	private String incident;
	
	IncidentType(String incident) {
		this.incident = incident;
	}
	
	public String getIncident() {
		return this.incident;
	}
}
