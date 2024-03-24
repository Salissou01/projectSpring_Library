package com.app.biblio.bean.request;

import java.time.LocalDateTime;

public class ReservationRequest {
    private String livreIsbn;
    private String userCin;
    
    private LocalDateTime dateReservation;

    // Getters et setters
    public String getLivreIsbn() {
        return livreIsbn;
    }

    public void setLivreIsbn(String livreIsbn) {
        this.livreIsbn = livreIsbn;
    }

    public String getUserCin() {
        return userCin;
    }

    public void setUserCin(String userCin) {
        this.userCin = userCin;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }
}
