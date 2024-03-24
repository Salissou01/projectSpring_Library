package com.app.biblio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.User;
import com.app.biblio.repository.EmpruntRepository;

@Service
public class EmpruntServiceImpl implements EmpruntService {

    @Autowired
    private EmpruntRepository empruntRepository;

    @Override
    public Emprunt create(Emprunt emprunt) {
        return empruntRepository.save(emprunt);
    }

    @Override
    public List<Emprunt> findAll() {
        return empruntRepository.findAll();
    }

    @Override
    public Emprunt findById(Long id) {
        return empruntRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        empruntRepository.deleteById(id);
    }

	@Override
	public Emprunt save(Emprunt emprunt) {
		 return empruntRepository.save(emprunt);
	}
	 @Override
	 public Page<Emprunt> getAllEmpruntByCin(String cin, Pageable pageable) {
	      
	        return empruntRepository.findByUserCIN(cin, pageable);
	    }

	@Override
	public Page<Emprunt> getAllEmprunt(Pageable pageable) {
		// TODO Auto-generated method stub
		return empruntRepository.findAll(pageable);
	}
	
	 @Override
	    public long countByStatut(StatutEmprunt statut) {
	        return empruntRepository.countByStatut(statut);
	    }

	
	    @Override
	    public List<Emprunt> findByUserAndStatut(User user, StatutEmprunt statut) {
	        return empruntRepository.findByUserAndStatut(user, statut);
	    }
	    @Override
	    public List<Emprunt> findAllEmpruntsWithStatusEmprunte() {
	        return empruntRepository.findByStatut(StatutEmprunt.EMPRUNTE);
	    }

}
