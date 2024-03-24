package com.app.biblio.bean;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LivreTest {

    private Livre livre;

    @BeforeEach
    public void setUp() {
        livre = new Livre();
    }

    @Test
    public void testId() {
        Long expectedId = 1L;
        livre.setId(expectedId);
        assertEquals(expectedId, livre.getId());
    }

    @Test
    public void testTitle() {
        String expectedTitle = "Test Livre";
        livre.setTitle(expectedTitle);
        assertEquals(expectedTitle, livre.getTitle());
    }

    @Test
    public void testAuthor() {
        String expectedAuthor = "Test Author";
        livre.setAuthor(expectedAuthor);
        assertEquals(expectedAuthor, livre.getAuthor());
    }

    @Test
    public void testISBN() {
        String expectedISBN = "1234567890";
        livre.setISBN(expectedISBN);
        assertEquals(expectedISBN, livre.getISBN());
    }

    @Test
    public void testPublicationDate() {
        Date expectedDate = new Date();
        livre.setPublicationDate(expectedDate);
        assertEquals(expectedDate, livre.getPublicationDate());
    }

    @Test
    public void testGenre() {
        String expectedGenre = "Test Genre";
        livre.setGenre(expectedGenre);
        assertEquals(expectedGenre, livre.getGenre());
    }

    @Test
    public void testAvailableCopies() {
        int expectedCopies = 5;
        livre.setAvailableCopies(expectedCopies);
        assertEquals(expectedCopies, livre.getAvailableCopies());
    }

    @Test
    public void testCategory() {
        Category expectedCategory = new Category();
        expectedCategory.setName("Test Category");
        livre.setCategory(expectedCategory);
        assertEquals(expectedCategory, livre.getCategory());
    }

    @Test
    public void testDescription() {
        String expectedDescription = "Test Description";
        livre.setDescription(expectedDescription);
        assertEquals(expectedDescription, livre.getDescription());
    }

    @Test
    public void testEditeur() {
        String expectedEditeur = "Test Editeur";
        livre.setEditeur(expectedEditeur);
        assertEquals(expectedEditeur, livre.getEditeur());
    }

    @Test
    public void testNombrePages() {
        int expectedPages = 200;
        livre.setNombrePages(expectedPages);
        assertEquals(expectedPages, livre.getNombrePages());
    }

    @Test
    public void testImageContent() {
        byte[] expectedImageContent = {0x01, 0x02, 0x03};
        livre.setImageContent(expectedImageContent);
        assertArrayEquals(expectedImageContent, livre.getImageContent());
    }
}
