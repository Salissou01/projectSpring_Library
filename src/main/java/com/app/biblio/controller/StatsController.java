package com.app.biblio.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.biblio.bean.StatutEmprunt;
import com.app.biblio.bean.StatutPenalite;
import com.app.biblio.bean.StatutReservation;
import com.app.biblio.bean.StatutRetour;
import com.app.biblio.service.EmpruntService;
import com.app.biblio.service.LivreService;
import com.app.biblio.service.PenaliteService;
import com.app.biblio.service.ReservationService;
import com.app.biblio.service.RetourService;
import com.app.biblio.service.UserService;

@Controller
public class StatsController {
	@Autowired
    private UserService userService;
	@Autowired
    private ReservationService reservationService;
	 @Autowired
	    private LivreService livreService;
	 @Autowired
	 private EmpruntService empruntService;
	 @Autowired 
	    private RetourService retourService;
	 @Autowired 
	    private PenaliteService penaliteService;

	 @GetMapping("/adminDashboard/stats")
	 @ResponseBody
	 public Map<String, Long> getStats() {
	     Map<String, Long> stats = new HashMap<>();
	     stats.put("userCount", userService.count());
	     stats.put("bookCount", livreService.count());
	     // les statistiques emprunts
	     stats.put("empruntConfirmedCount", empruntService.countByStatut(StatutEmprunt.EMPRUNTE));
	     stats.put("empruntRejeteCount", empruntService.countByStatut(StatutEmprunt.REJETE));
	     stats.put("empruntEnAttenteCount", empruntService.countByStatut(StatutEmprunt.EN_ATTENTE));
	     stats.put("empruntRetourneCount", empruntService.countByStatut(StatutEmprunt.RETOURNE));
	   
	     //  les statistiques de retour
	        stats.put("retourEnRetardCount", retourService.countByStatut(StatutRetour.EN_RETARD));
	        stats.put("retourALHeureCount", retourService.countByStatut(StatutRetour.A_L_HEURE));
	        stats.put("retourEnAttenteCount", retourService.countByStatut(StatutRetour.EN_ATTENTE));
	        // les statistiques pour reservation
	        stats.put("reservationEnAttenteCount", reservationService.countByStatut(StatutReservation.EN_ATTENTE));
	        stats.put("reservationValideCount", reservationService.countByStatut(StatutReservation.VALIDE));
	        stats.put("reservationRetireCount", reservationService.countByStatut(StatutReservation.RETIRE));
	        stats.put("reservationAnnuleCount", reservationService.countByStatut(StatutReservation.ANNULE));
	        //Les statistiques pour penalites
	        stats.put("penalitePayeeCount", penaliteService.countByStatut(StatutPenalite.PAYEE));
	        stats.put("penaliteNonPayeeCount", penaliteService.countByStatut(StatutPenalite.NON_PAYEE));
	        
	     return stats;
	 }


}
