package com.example.ObservaLite.entities.enums;

public enum ProjectStatus {
	HEALTHY("Healthy"), WARNING("Warning"), DOWN("Down"), DEGRADED("Degraded");
	
	private String status;
	
	ProjectStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return this.status;
	}
}
