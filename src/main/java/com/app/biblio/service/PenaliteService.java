package com.app.biblio.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.biblio.bean.Penalite;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutPenalite;

public interface PenaliteService {
    Penalite save(Penalite penalite);

	

	Penalite findByRetour(Retour retour);



	Page<Penalite> getAllPenaliteByCin(String cin, Pageable pageable);



	Page<Penalite> getAllPenalite(Pageable pageable);



	Penalite findById(Long id);



	List<Penalite> findAll();



	Penalite findByIdWithAssociations(Long id);



	boolean hasUnpaidPenalties(String cin);



	long countByStatut(StatutPenalite statut);



}
