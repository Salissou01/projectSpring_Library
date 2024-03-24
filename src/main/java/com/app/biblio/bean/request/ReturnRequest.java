package com.app.biblio.bean.request;
public class ReturnRequest {
    private String dateRetourEffectif;
    private Integer note;
	public String getDateRetourEffectif() {
		return dateRetourEffectif;
	}
	public void setDateRetourEffectif(String dateRetourEffectif) {
		this.dateRetourEffectif = dateRetourEffectif;
	}
	public Integer getNote() {
		return note;
	}
	public void setNote(Integer note) {
		this.note = note;
	}

  
}
