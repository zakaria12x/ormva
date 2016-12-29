package com.ormva.pojo;

import java.io.Serializable;
import java.util.Date;



public class Immobilisation implements Serializable{
	public Integer id;
	private String designation;
	private String codeABarre;
	private String date_acquis;
	private String date_mis;
	private String commentaire;
	
	public String getDate_acquis() {
		return date_acquis;
	}
	public void setDate_acquis(String date_acquis) {
		this.date_acquis = date_acquis;
	}
	public String getDate_mis() {
		return date_mis;
	}
	public void setDate_mis(String date_mis) {
		this.date_mis = date_mis;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getCodeABarre() {
		return codeABarre;
	}
	public void setCodeABarre(String codeABarre) {
		this.codeABarre = codeABarre;
	}
	public Immobilisation() {
		super();
	}
	public Immobilisation(Integer id, String designation, String codeABarre) {
		super();
		this.id = id;
		this.designation = designation;
		this.codeABarre = codeABarre;
	}
	
	public Immobilisation(Integer id, String designation, String codeABarre, String date_acquis, String date_mis) {
		this.id = id;
		this.designation = designation;
		this.codeABarre = codeABarre;
		this.date_acquis = date_acquis;
		this.date_mis = date_mis;
	}
	
	public Immobilisation(Integer id, String designation, String codeABarre, String date_acquis, String date_mis,
			String commentaire) {
		this.id = id;
		this.designation = designation;
		this.codeABarre = codeABarre;
		this.date_acquis = date_acquis;
		this.date_mis = date_mis;
		this.commentaire = commentaire;
	}
	@Override
	public String toString() {
		return designation ;
	}
	
    
}
