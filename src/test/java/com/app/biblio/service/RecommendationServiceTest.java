package com.app.biblio.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.biblio.bean.Livre;
import com.app.biblio.service.RecommendationService;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

    @Mock
    private RecommendationService recommendationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRecommendBooksBasedOnRatings() {
        // Préparation
        Map<Livre, Double> expectedRecommendations = mock(Map.class);
        Livre livre1 = new Livre();
        livre1.setId(1L);
        Livre livre2 = new Livre();
        livre2.setId(2L);
        expectedRecommendations.put(livre1, 4.5);
        expectedRecommendations.put(livre2, 4.0);

        
        when(recommendationService.recommendBooksBasedOnRatings()).thenReturn(expectedRecommendations);

        // Exécution
        Map<Livre, Double> result = recommendationService.recommendBooksBasedOnRatings();

        // Vérification
        assertNotNull(result);
        assertEquals(expectedRecommendations.size(), result.size());
        for (Livre livre : expectedRecommendations.keySet()) {
            assertNotNull(result.get(livre));
            assertEquals(expectedRecommendations.get(livre), result.get(livre));
        }
    }
}
