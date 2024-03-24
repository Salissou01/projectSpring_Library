package com.app.biblio.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.biblio.bean.Penalite;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutPenalite;

@Repository
public interface PenaliteRepository extends JpaRepository<Penalite, Long> {
	 @Query("SELECT p FROM Penalite p JOIN FETCH p.retour r JOIN FETCH r.emprunt e JOIN FETCH e.livre WHERE p.id = :id")
	  Optional<Penalite> findByIdWithAssociations(@Param("id") Long id);
	Penalite findByRetour(Retour retour);
	 Page<Penalite> findByRetourEmpruntUserCIN(String cin, Pageable pageable);
	boolean existsByRetourEmpruntUserCINAndStatutPenalite(String cin, StatutPenalite nonPayee);
	long countByStatutPenalite(StatutPenalite statut);
}
