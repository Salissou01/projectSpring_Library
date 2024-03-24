package com.app.biblio.service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.biblio.bean.Livre;
import com.app.biblio.repository.LivreRepository;

@Service
public class LivreServiceImpl implements LivreService {

    @Autowired
    private LivreRepository livreRepository;

    @Override
    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }
    @Override
    public Livre save(Livre livre) {
        System.out.println("Sauvegarde du livre avec ID : " + livre.getId() + " et disponibleCopies : " + livre.getAvailableCopies());
        return livreRepository.save(livre);
    }

	  @Override
	    public Livre findById(Long id) {
	        return livreRepository.findById(id).orElse(null);
	    }
	  public byte[] getImageContentById(Long id) throws FileNotFoundException {
		    Optional<Livre> livreOptional = livreRepository.findById(id);

		    if (livreOptional.isPresent()) {
		        Livre livre = livreOptional.get();
		        return livre.getImageContent(); 
		    } else {
		        // Gérer le cas où aucun livre avec l'ID spécifié n'est trouvé
		        throw new FileNotFoundException("Livre non trouvé avec l'ID : " + id);
		    }
		}

	   @Override
	    public void delete(Livre livre) {
	        livreRepository.delete(livre);
	    }
	   @Override
	    public Page<Livre> getAllLivres(Pageable pageable) {
	        return livreRepository.findAll(pageable);
	    }

	    public long count() {
	        return livreRepository.count();
	    }

		@Override
		public Livre findByIsbn(String isbn) {
			return livreRepository.findByISBN(isbn);
		}

}
