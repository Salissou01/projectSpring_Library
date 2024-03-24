document.getElementById('searchInput').addEventListener('keyup', function() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchInput");
    filter = input.value.toUpperCase();
    table = document.getElementById("livresTable");
    tr = table.getElementsByTagName("tr");

    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td");
        for (var j = 0; j < td.length; j++) {
            if (td[j]) {
                txtValue = td[j].textContent || td[j].innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    tr[i].style.display = "";
                    break;
                } else {
                    tr[i].style.display = "none";
                }
            }       
        }
    }
});

function confirmDelete(id) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce livre ?')) {
        window.location.href = '/adminDashboard/livres/delete/' + id;
    }
}


$(document).ready(function() {
    $('#livresTable').DataTable({
        "pageLength": 3, // Nombre de lignes par page
        "pagingType": "simple_numbers", // Type de pagination
        "language": {
            "url": "//cdn.datatables.net/plug-ins/1.11.5/i18n/French.json" // Langue française
        }
    });
});


