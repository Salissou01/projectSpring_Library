package com.app.biblio.service;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmpruntService {
    Emprunt create(Emprunt emprunt);
    List<Emprunt> findAll();
    Emprunt findById(Long id);
    void delete(Long id);
	Emprunt save(Emprunt emprunt);
	Page<Emprunt> getAllEmprunt(Pageable pageable);
	Page<Emprunt> getAllEmpruntByCin(String cin, Pageable pageable);

		long countByStatut(StatutEmprunt statut);
		List<Emprunt> findByUserAndStatut(User user, StatutEmprunt statut);
		List<Emprunt> findAllEmpruntsWithStatusEmprunte();
}
