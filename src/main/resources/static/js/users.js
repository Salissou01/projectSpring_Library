
    function deleteUser(userId) {
        if (confirm("Êtes-vous sûr de vouloir supprimer cet utilisateur ?")) {
            
            fetch('/adminDashboard/users/delete/' + userId, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                   
                    location.reload(); 
                } else {
                    alert("Une erreur s'est produite lors de la suppression de l'utilisateur.");
                }
            })
            .catch(error => {
                console.error('Erreur lors de la suppression de l\'utilisateur:', error);
                alert("Une erreur s'est produite lors de la suppression de l'utilisateur.");
            });
        }
    }

   
    var searchInput = document.getElementById('searchInput');

  
    var userRows = document.querySelectorAll('#usersTable tbody tr');


    searchInput.addEventListener('input', function() {
        var searchText = searchInput.value.toLowerCase();

        
        userRows.forEach(function(row) {
            var cin = row.cells[0].textContent.toLowerCase();
            var username = row.cells[1].textContent.toLowerCase();
            var email = row.cells[2].textContent.toLowerCase();
            var telephone = row.cells[3].textContent.toLowerCase();

            
            if (cin.includes(searchText) || username.includes(searchText) || email.includes(searchText) || telephone.includes(searchText)) {
                
                row.style.display = '';
            } else {
               
                row.style.display = 'none';
            }
        });
    });

