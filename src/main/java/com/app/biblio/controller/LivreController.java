package com.app.biblio.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.biblio.bean.Category;
import com.app.biblio.bean.Livre;
import com.app.biblio.service.CategoryService;
import com.app.biblio.service.LivreService;

@Controller
@RequestMapping("/adminDashboard")
public class LivreController {

    @Autowired
    private LivreService livreService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/livres")
    public String listeLivres(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 4); 
        Page<Livre> livres = livreService.getAllLivres(pageable);
        model.addAttribute("livres", livres);
        return "livre";
    }
    @GetMapping("/livres/add")
    public String showAddLivreForm(Model model) {
        Livre livre = new Livre();
        model.addAttribute("livre", livre);

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "addLivre"; 
    }
    @PostMapping("/livres/add")
    public String addLivre(@ModelAttribute("livre") Livre livre, @RequestParam("imagePath") MultipartFile file, @RequestParam("category") Long categoryId, Model model) {
       
        if (!file.isEmpty()) {
            try {
                
                byte[] imageBytes = file.getBytes();
              
                livre.setImageContent(imageBytes);

                Category category = categoryService.findById(categoryId);
                livre.setCategory(category); 

                livreService.save(livre);

                return "redirect:/adminDashboard/livres";
            } catch (IOException e) {
                model.addAttribute("error", "Une erreur s'est produite lors de l'enregistrement de l'image.");
                return "redirect:/adminDashboard/livres/add"; 
            }
        } else {
            model.addAttribute("error", "Veuillez sélectionner une image pour le livre.");
            return "redirect:/adminDashboard/livres/add"; 
        }
    }


   
    @GetMapping("/livres/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws FileNotFoundException {
    
        byte[] imageData = livreService.getImageContentById(id);
        
  
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
    }

    @GetMapping("/livres/details/{id}")
    public String showLivreDetails(@PathVariable("id") Long id, Model model) {
      
        Livre livre = livreService.findById(id);
        
     
        if (livre == null) {
            
            return "livreNotFound";
        }
        
 
        model.addAttribute("livre", livre);
        
       
        return "livreDetails"; 
    }
    @GetMapping("/livres/edit/{id}")
    public String showEditLivreForm(@PathVariable("id") Long id, Model model) {
        Livre livre = livreService.findById(id);
        if (livre == null) {
            return "livreNotFound";
        }
        model.addAttribute("livre", livre);

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "editLivre"; 
    }

    @PostMapping("/livres/edit/{id}")
    public String editLivre(@PathVariable("id") Long id, @ModelAttribute("livre") Livre livre, @RequestParam("imagePath") MultipartFile file, @RequestParam("category") Long categoryId, Model model) {
        Livre existingLivre = livreService.findById(id);
        if (existingLivre == null) {
            return "livreNotFound";
        }
        existingLivre.setTitle(livre.getTitle());
        existingLivre.setAuthor(livre.getAuthor());
        existingLivre.setISBN(livre.getISBN());
        existingLivre.setPublicationDate(livre.getPublicationDate());
        existingLivre.setGenre(livre.getGenre());
        existingLivre.setAvailableCopies(livre.getAvailableCopies());
        existingLivre.setDescription(livre.getDescription());
        existingLivre.setEditeur(livre.getEditeur());
        existingLivre.setNombrePages(livre.getNombrePages());

        if (!file.isEmpty()) {
            try {
                
                byte[] imageBytes = file.getBytes();
               
                existingLivre.setImageContent(imageBytes);
            } catch (IOException e) {
                model.addAttribute("error", "Une erreur s'est produite lors de l'enregistrement de l'image.");
                return "redirect:/adminDashboard/livres/edit/" + id; 
            }
        }

        Category category = categoryService.findById(categoryId);
        existingLivre.setCategory(category); 
        livreService.save(existingLivre);

       
        return "redirect:/adminDashboard/livres";
    }
    @GetMapping("/livres/delete/{id}")
    public String deleteLivre(@PathVariable("id") Long id, Model model) {
        Livre livre = livreService.findById(id);
        if (livre != null) {
            livreService.delete(livre);
            model.addAttribute("message", "Le livre a été supprimé avec succès.");
        } else {
            model.addAttribute("error", "Le livre n'a pas été trouvé.");
        }
        return "redirect:/adminDashboard/livres"; 
    }


}
