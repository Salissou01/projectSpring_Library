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


import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="t_emprunts")
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "livre_id")
    private Livre livre;

    private int nombreExemplaires;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "date_emprunt")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateEmprunt;

    @Column(name = "date_retour_prevu")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateRetourPrevu;
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    public StatutEmprunt statut = StatutEmprunt.EN_ATTENTE;



    public int getNombreExemplaires() {
		return nombreExemplaires;
	}

	public void setNombreExemplaires(int nombreExemplaires) {
		this.nombreExemplaires = nombreExemplaires;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StatutEmprunt getStatut() {
		return statut;
	}

	public void setStatut(StatutEmprunt statut) {
		this.statut = statut;
	}

	public Livre getLivre() {
		return livre;
	}

	public void setLivre(Livre livre) {
		this.livre = livre;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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


  
}
