package com.app.biblio.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.bean.User;

@Repository
public interface RetourRepository extends JpaRepository<Retour, Long> {

	Page<Retour> findByEmpruntUserCIN(String cin, Pageable pageable);
	  
    List<Retour> findByEmpruntUser(User user);
	List<Retour> findByEmpruntLivre(Livre livre);

    @Query("SELECT r FROM Retour r JOIN FETCH r.emprunt e JOIN FETCH e.livre WHERE r.id = :id")
    Optional<Retour> findByIdWithAssociations(@Param("id") Long id);
	long countByStatutRetour(StatutRetour statut);

  
}