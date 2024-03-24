package com.app.biblio.bean.request;

import java.util.Date;

public class EmpruntRequest {
    private Date dateEmprunt;
    private Date dateRetourPrevu;
    private String cin;
    private String isbn;
    private int nombreExemplaires;
	public int getNombreExemplaires() {
		return nombreExemplaires;
	}
	public void setNombreExemplaires(int nombreExemplaires) {
		this.nombreExemplaires = nombreExemplaires;
	}
	public Date getDateEmprunt() {
		return dateEmprunt;
	}
	public void setDateEmprunt(Date dateEmprunt) {
		this.dateEmprunt = dateEmprunt;
	}
	public Date getDateRetourPrevu() {
		return dateRetourPrevu;
	}
	public void setDateRetourPrevu(Date dateRetourPrevu) {
		this.dateRetourPrevu = dateRetourPrevu;
	}
	public String getCin() {
		return cin;
	}
	public void setCin(String cin) {
		this.cin = cin;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

}
