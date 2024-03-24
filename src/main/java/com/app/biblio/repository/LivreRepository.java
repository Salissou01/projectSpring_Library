package com.app.biblio.repository;

import com.app.biblio.bean.Livre;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {
	Livre findByISBN(String ISBN);
    
}
