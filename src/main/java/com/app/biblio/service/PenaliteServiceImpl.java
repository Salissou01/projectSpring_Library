package com.app.biblio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.biblio.bean.Penalite;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutPenalite;
import com.app.biblio.repository.PenaliteRepository;


@Service
public class PenaliteServiceImpl implements PenaliteService {
	@Autowired
    private PenaliteRepository penaliteRepository;

    @Override
    public Penalite findByRetour(Retour retour) {
        return penaliteRepository.findByRetour(retour);
    }

    @Override
    public Penalite save(Penalite penalite) {
        return penaliteRepository.save(penalite);
    }
    @Override
    public Page<Penalite> getAllPenaliteByCin(String cin, Pageable pageable) {
        return penaliteRepository.findByRetourEmpruntUserCIN(cin, pageable);
    }
    @Override
    public boolean hasUnpaidPenalties(String cin) {
       
        return penaliteRepository.existsByRetourEmpruntUserCINAndStatutPenalite(cin, StatutPenalite.NON_PAYEE);
    }
    


    @Override
    public Page<Penalite> getAllPenalite(Pageable pageable) {
        return penaliteRepository.findAll(pageable);
    }

	@Override
	public Penalite findById(Long id) {
		// TODO Auto-generated method stub
		return penaliteRepository.findById(id).orElse(null);
	}

	@Override
	public List<Penalite> findAll() {
		return penaliteRepository.findAll();
	}
	  @Override
    public Penalite findByIdWithAssociations(Long id) {
        Optional<Penalite> optionalPenalite = penaliteRepository.findByIdWithAssociations(id);
        return optionalPenalite.orElse(null);
    }
	  
	   @Override
	    public long countByStatut(StatutPenalite statut) {
	        return penaliteRepository.countByStatutPenalite(statut);
	    }
}
