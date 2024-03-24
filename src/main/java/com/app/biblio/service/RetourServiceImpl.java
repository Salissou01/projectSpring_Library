
package com.app.biblio.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.repository.RetourRepository;


@Service
public class RetourServiceImpl implements RetourService {

    @Autowired
    private RetourRepository retourRepository;

	@Override
	public Retour save(Retour retour) {
		return retourRepository.save(retour);
		
	}
	 @Override
	 public Page<Retour> getAllRetourByCin(String cin, Pageable pageable) {
	       
	        return retourRepository.findByEmpruntUserCIN(cin, pageable);
	    }

	@Override
	public Page<Retour> getAllRetour(Pageable pageable) {
	
		return retourRepository.findAll(pageable);
	}
	@Override
	public Retour findById(Long id) {
		return retourRepository.getById(id);
	}
	  @Override
	    public Retour findByIdWithAssociations(Long id) {
	        Optional<Retour> optionalRetour = retourRepository.findByIdWithAssociations(id);
	        return optionalRetour.orElse(null);
	    }
	  
	  @Override
	  public long countByStatut(StatutRetour statut) {
	      return retourRepository.countByStatutRetour(statut);
	  }

}
