package com.app.biblio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.biblio.bean.Penalite;
import com.app.biblio.bean.Retour;
import com.app.biblio.bean.StatutPenalite;
import com.app.biblio.repository.PenaliteRepository;

@ExtendWith(MockitoExtension.class)
public class PenaliteServiceUnitTest {

    @Mock
    private PenaliteRepository penaliteRepository;

    @InjectMocks
    private PenaliteServiceImpl penaliteService;

    @Test
    public void testFindByRetour() {
        // Given
        Retour retour = new Retour();
        // Ajoutez ici la logique pour initialiser retour si nécessaire

        Penalite expectedPenalite = new Penalite();
        expectedPenalite.setMontant(new BigDecimal("10.0"));
        expectedPenalite.setStatutPenalite(StatutPenalite.NON_PAYEE);
        // Ajoutez ici la logique pour initialiser expectedPenalite si nécessaire

        when(penaliteRepository.findByRetour(retour)).thenReturn(expectedPenalite);

        // When
        Penalite actualPenalite = penaliteService.findByRetour(retour);

        // Then
        assertThat(actualPenalite).isEqualTo(expectedPenalite);
        // Vous pouvez ajouter des assertions supplémentaires si nécessaire
    }
}
