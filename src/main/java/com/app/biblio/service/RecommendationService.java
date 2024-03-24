package com.app.biblio.service;

import java.util.Map;

import com.app.biblio.bean.Livre;


public interface RecommendationService {
	Map<Livre, Double> recommendBooksBasedOnRatings();
}
