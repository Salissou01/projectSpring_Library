$(document).ready(function() {
    function updateStats() {
        $.get("/adminDashboard/stats", function(data) {
            $("#userCount").text(data.userCount);
            $("#bookCount").text(data.bookCount);
          
            $("#empruntConfirmedCount").text(data.empruntConfirmedCount);
        
            $("#empruntRejeteCount").text(data.empruntRejeteCount);
    
            $("#empruntEnAttenteCount").text(data.empruntEnAttenteCount);
    
            $("#empruntRetourneCount").text(data.empruntRetourneCount);
        
            $("#retourEnRetardCount").text(data.retourEnRetardCount);
            $("#retourALHeureCount").text(data.retourALHeureCount);
            $("#retourEnAttenteCount").text(data.retourEnAttenteCount);
            
            $("#reservationEnAttenteCount").text(data.reservationEnAttenteCount);
            $("#reservationValideCount").text(data.reservationValideCount);
            $("#reservationRetireCount").text(data.reservationRetireCount);
            $("#reservationAnnuleCount").text(data.reservationAnnuleCount);
            $("#penalitePayeeCount").text(data.penalitePayeeCount);
            $("#penaliteNonPayeeCount").text(data.penaliteNonPayeeCount);
          
        });
    }

    
    updateStats();

   
    setInterval(updateStats, 5000);
});
