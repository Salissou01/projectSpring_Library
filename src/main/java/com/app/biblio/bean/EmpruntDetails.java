package com.app.biblio.bean;
public class EmpruntDetails {
    private String livreTitle;
    private String userName;
    private String dateRetourPrevu;

    public EmpruntDetails(Emprunt emprunt) {
        this.livreTitle = emprunt.getLivre().getTitle();
        this.userName = emprunt.getUser().getUsername();
        this.dateRetourPrevu = emprunt.getDateRetourPrevu().toString(); 
    }

    // Getters et setters
    public String getLivreTitle() {
        return livreTitle;
    }

    public String getUserName() {
        return userName;
    }

    public String getDateRetourPrevu() {
        return dateRetourPrevu;
    }
}
