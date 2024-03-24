package com.app.biblio.service;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.biblio.bean.Livre;

public interface LivreService {
    List<Livre> getAllLivres();

	Livre save(Livre livre);

	Livre findById(Long id);

    void delete(Livre livre);

	byte[] getImageContentById(Long id) throws FileNotFoundException;
	  Page<Livre> getAllLivres(Pageable pageable);

	long count();

	Livre findByIsbn(String isbn);
}
