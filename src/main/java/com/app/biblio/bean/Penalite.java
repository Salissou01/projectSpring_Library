package com.app.biblio.bean;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "penalites")
public class Penalite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Retour getRetour() {
		return retour;
	}

	public void setRetour(Retour retour) {
		this.retour = retour;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "retour_id", nullable = false)
    private Retour retour;

    @Column(name = "montant", nullable = false)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_penalite", nullable = false)
    private StatutPenalite statutPenalite; 


    public Penalite() {
    }

    public Penalite(Retour retour, BigDecimal montant, StatutPenalite statutPenalite) {
        this.retour = retour;
        this.montant = montant;
        this.statutPenalite = statutPenalite;
    }

   
    public StatutPenalite getStatutPenalite() {
        return statutPenalite;
    }

    public void setStatutPenalite(StatutPenalite statutPenalite) {
        this.statutPenalite = statutPenalite;
    }

}
