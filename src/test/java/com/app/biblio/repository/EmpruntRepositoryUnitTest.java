package com.app.biblio.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.biblio.bean.Emprunt;
import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.User;
import com.app.biblio.service.EmpruntServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmpruntRepositoryUnitTest {

    @Mock
    private EmpruntRepository empruntRepository;

    @InjectMocks
    private EmpruntServiceImpl empruntService;
    @Test
    public void testFindByUserAndStatut() {
        // Given
        User user = new User();
        user.setId(1L);
        StatutEmprunt statut = StatutEmprunt.EMPRUNTE;
        List<Emprunt> expectedEmprunts = Collections.emptyList();
        when(empruntRepository.findByUserAndStatut(user, statut)).thenReturn(expectedEmprunts);

        // When
        List<Emprunt> actualEmprunts = empruntService.findByUserAndStatut(user, statut);

        // Then
        assertThat(actualEmprunts).isEqualTo(expectedEmprunts);
    }

    @Test
    public void testCountByStatut() {
        // Given
        StatutEmprunt statut = StatutEmprunt.EMPRUNTE;
        long expectedCount = 5;
        when(empruntRepository.countByStatut(statut)).thenReturn(expectedCount);

        // When
        long actualCount = empruntService.countByStatut(statut);

        // Then
        assertThat(actualCount).isEqualTo(expectedCount);
    }
}
