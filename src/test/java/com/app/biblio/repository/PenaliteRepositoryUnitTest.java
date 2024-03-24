package com.app.biblio.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.biblio.bean.Penalite;
import com.app.biblio.service.PenaliteServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PenaliteRepositoryUnitTest {

    @Mock
    private PenaliteRepository penaliteRepository;

    @InjectMocks
    private PenaliteServiceImpl penaliteService;

    @Test
    public void testFindByIdWithAssociations() {
        // Given
        Long id = 1L;
        Penalite expectedPenalite = new Penalite();
        expectedPenalite.setId(id);
        // Ajoutez ici la logique pour initialiser les associations de expectedPenalite

        when(penaliteRepository.findByIdWithAssociations(id)).thenReturn(Optional.of(expectedPenalite));

        // When
        Optional<Penalite> actualPenalite = Optional.ofNullable(penaliteService.findByIdWithAssociations(id));

        // Then
        assertThat(actualPenalite).isPresent();
        assertThat(actualPenalite.get()).isEqualTo(expectedPenalite);
        // Vous pouvez ajouter des assertions supplémentaires pour vérifier les associations chargées
    }
}
