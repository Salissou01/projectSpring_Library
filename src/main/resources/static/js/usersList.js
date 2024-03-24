
document.getElementById('showUsersLink').addEventListener('click', function(event) {
    event.preventDefault(); 
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/adminDashboard/users');
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
        if (xhr.status === 200) {
            var users = JSON.parse(xhr.responseText);

            var userTableBody = document.getElementById('userTableBody');
            userTableBody.innerHTML = '';
            users.forEach(function(user) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + user.username + '</td>' +
                                '<td>' + user.email + '</td>';
                userTableBody.appendChild(row);
            });

            document.getElementById('userTableContainer').style.display = 'block';
        } else {
            console.error('Erreur lors de la récupération des utilisateurs: ' + xhr.status);
        }
    };
    xhr.send();
});
