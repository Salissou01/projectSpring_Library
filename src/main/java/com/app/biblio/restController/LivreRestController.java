package com.app.biblio.restController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.app.biblio.bean.Category;
import com.app.biblio.bean.Livre;
import com.app.biblio.bean.LivreDTO;
import com.app.biblio.service.CategoryService;
import com.app.biblio.service.LivreService;

@RestController
@RequestMapping("/api/livres")
public class LivreRestController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private CategoryService categoryService;
   
    @GetMapping
    public ResponseEntity<List<Livre>> listeLivres() {
        List<Livre> livres = livreService.getAllLivres();
        return new ResponseEntity<>(livres, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addLivre(@RequestBody LivreDTO livreDTO) {
        try {
            Livre livre = new Livre();
          
            livre.setTitle(livreDTO.getTitle());
            livre.setAuthor(livreDTO.getAuthor());
            livre.setISBN(livreDTO.getISBN());
            livre.setPublicationDate(livreDTO.getPublicationDate());
            livre.setGenre(livreDTO.getGenre());
            livre.setAvailableCopies(livreDTO.getAvailableCopies());
            livre.setDescription(livreDTO.getDescription());
            livre.setEditeur(livreDTO.getEditeur());
            livre.setNombrePages(livreDTO.getNombrePages());

           
            Path path = Paths.get(livreDTO.getImagePath());
         
            byte[] imageBytes = Files.readAllBytes(path);
            livre.setImageContent(imageBytes);

            Category category = categoryService.findById(livreDTO.getCategoryId());
            livre.setCategory(category);
            livreService.save(livre);
            return new ResponseEntity<>("Livre créé avec succès", HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Erreur lors de la création du livre", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Livre> getLivre(@PathVariable Long id) {
        Livre livre = livreService.findById(id);
        if (livre == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(livre, HttpStatus.OK);
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editLivre(@PathVariable Long id, @RequestBody LivreDTO livreDTO) {
        Livre existingLivre = livreService.findById(id);
        if (existingLivre == null) {
            return new ResponseEntity<>("Livre non trouvé", HttpStatus.NOT_FOUND);
        }
        try {
           
            existingLivre.setTitle(livreDTO.getTitle());
            existingLivre.setAuthor(livreDTO.getAuthor());
            existingLivre.setISBN(livreDTO.getISBN());
            existingLivre.setPublicationDate(livreDTO.getPublicationDate());
            existingLivre.setGenre(livreDTO.getGenre());
            existingLivre.setAvailableCopies(livreDTO.getAvailableCopies());
            existingLivre.setDescription(livreDTO.getDescription());
            existingLivre.setEditeur(livreDTO.getEditeur());
            existingLivre.setNombrePages(livreDTO.getNombrePages());
            Path path = Paths.get(livreDTO.getImagePath());
         
            byte[] imageBytes = Files.readAllBytes(path);
            existingLivre.setImageContent(imageBytes);
           
            Category category = categoryService.findById(livreDTO.getCategoryId());
            existingLivre.setCategory(category);

            Livre updatedLivre = livreService.save(existingLivre);
            return new ResponseEntity<>(updatedLivre, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la mise à jour du livre", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLivre(@PathVariable Long id) {
        Livre livre = livreService.findById(id);
        if (livre == null) {
            return new ResponseEntity<>("Livre non trouvé", HttpStatus.NOT_FOUND);
        }
        livreService.delete(livre);
        return new ResponseEntity<>("Livre supprimé avec succès", HttpStatus.NO_CONTENT);
    }

}
