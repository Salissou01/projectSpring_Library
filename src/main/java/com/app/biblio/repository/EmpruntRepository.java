package com.app.biblio.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.User;
@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
	Page<Emprunt> findByUserCIN(String cin, Pageable pageable);


	long countByStatut(StatutEmprunt statut);
	  List<Emprunt> findByUserAndStatut(User user, StatutEmprunt statut);

	List<Emprunt> findByStatut(StatutEmprunt statut);
}
