package com.ormva.pojo;

public class Service {
	private Integer id;
	private String service;
	
	public Service() {
		super();
	}
	public Service(Integer id, String service) {
		super();
		this.id = id;
		this.service = service;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	@Override
	public String toString() {
		return "Service " + service ;
	}
	
	
}
