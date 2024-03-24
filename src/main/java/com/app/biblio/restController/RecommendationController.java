package com.app.biblio.restController;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.biblio.service.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
	  @Autowired
    private  RecommendationService recommendationService;
   
	  /*@GetMapping
	  public ResponseEntity<List<LivreDTO>> getRecommendedBooks() {
	      // Récupérer les livres recommandés
	      Map<Livre, Double> recommendedBooksMap = recommendationService.recommendBooksBasedOnRatings();
	      
	      // Transformer les objets Livre en LivreDTO
	      List<LivreDTO> recommendedBooksDTO = recommendedBooksMap.entrySet().stream()
	              .map(entry -> {
	                  Livre livre = entry.getKey();
	                  Double score = entry.getValue();
	                  LivreDTO livreDTO = new LivreDTO();
	                  livreDTO.setTitle(livre.getTitle());
	                  livreDTO.setAuthor(livre.getAuthor());
	                  livreDTO.setISBN(livre.getISBN());
	                  livreDTO.setPublicationDate(livre.getPublicationDate());
	                  livreDTO0
	                  livreDTO.setGenre(livre.getGenre());
	                  // Ajouter le score de recommandation comme un attribut supplémentaire
	                  livreDTO.setScore(score);
	                  return livreDTO;
	              })
	              .collect(Collectors.toList());
	      
	      return ResponseEntity.ok(recommendedBooksDTO);
	  }
    */
    
}
