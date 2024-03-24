package com.app.biblio.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutRetour;

public interface RetourService {

	Retour save(Retour retour);
	Page<Retour> getAllRetour(Pageable pageable);
	Page<Retour> getAllRetourByCin(String cin, Pageable pageable);
	Retour findById(Long id);
	Retour findByIdWithAssociations(Long id);
	long countByStatut(StatutRetour statut);


  
}
