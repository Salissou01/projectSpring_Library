package com.app.biblio.bean;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "Retour")
public class Retour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emprunt_id", nullable = false)
    private Emprunt emprunt;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_retour", nullable = false)
    private StatutRetour statutRetour;

    @Column(name = "date_retour_effectif")
    private Date dateRetourEffectif;


    @Column(name = "note")
    private Integer note;

    // Getters and Setters

    

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Emprunt getEmprunt() {
		return emprunt;
	}

	public void setEmprunt(Emprunt emprunt) {
		this.emprunt = emprunt;
	}

	public StatutRetour getStatutRetour() {
		return statutRetour;
	}

	public void setStatutRetour(StatutRetour statutRetour) {
		this.statutRetour = statutRetour;
	}

	public Date getDateRetourEffectif() {
		return dateRetourEffectif;
	}

	public void setDateRetourEffectif(Date dateRetourEffectif) {
		this.dateRetourEffectif = dateRetourEffectif;
	}

	
    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }
}
