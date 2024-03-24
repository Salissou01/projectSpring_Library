package com.app.biblio.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Retour;

import com.app.biblio.repository.RetourRepository;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private RetourRepository retourRepository;

    @Override
    public Map<Livre, Double> recommendBooksBasedOnRatings() {
   
        List<Retour> allReturns = retourRepository.findAll();

        Map<Livre, Double> livreAverageRatings = allReturns.stream()
                .filter(retour -> retour.getNote() != null)
                .collect(Collectors.groupingBy(retour -> retour.getEmprunt().getLivre(),
                        Collectors.averagingInt(Retour::getNote)));

        Map<Livre, Double> recommendedBooks = livreAverageRatings.entrySet().stream()
                .filter(entry -> entry.getValue() > 3)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Math.round(entry.getValue() * 100.0) / 100.0));

        return recommendedBooks;
    }


    public double calculateAverageRating(Livre livre) {
        List<Retour> livreReturns = retourRepository.findByEmpruntLivre(livre);
        if (livreReturns.isEmpty()) {
            return 0; 
        }
        double averageRating = livreReturns.stream()
                .filter(retour -> retour.getNote() != null)
                .mapToInt(Retour::getNote)
                .average()
                .orElse(0); 
        return Math.round(averageRating * 100.0) / 100.0;
    }

}
