package com.app.biblio.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.biblio.bean.Retour;
import com.app.biblio.service.RetourServiceImpl;

@ExtendWith(MockitoExtension.class)
public class RetourRepositoryUnitTest {

    @Mock
    private RetourRepository retourRepository;

    @InjectMocks
    private RetourServiceImpl retourService;

    @Test
    public void testGetAllRetourByCin() {
        // Given
        String cin = "123456789";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Retour> expectedPage = new PageImpl<>(Collections.emptyList());
        when(retourRepository.findByEmpruntUserCIN(cin, pageable)).thenReturn(expectedPage);

        // When
        Page<Retour> actualPage = retourService.getAllRetourByCin(cin, pageable);

        // Then
        assertThat(actualPage).isEqualTo(expectedPage);
    }

    @Test
    public void testFindByIdWithAssociations() {
        // Given
        Long id = 1L;
        Optional<Retour> expectedRetour = Optional.empty();
        when(retourRepository.findByIdWithAssociations(id)).thenReturn(expectedRetour);

        // When
        Optional<Retour> actualRetour = Optional.ofNullable(retourService.findByIdWithAssociations(id));

        // Then
        assertThat(actualRetour).isEqualTo(expectedRetour);
    }
}
