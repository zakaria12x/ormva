package com.ormva.pojo;


	public class Bureau {
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Bureau() {
		super();
	}
	public Bureau(Integer id) {
		super();
		this.id = id;
	}

	@Override
	public String toString() {
		return "Bureau " + id ;
	}
	
	}
