<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion des Alertes - Multi-colonnes</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; background-color: #f9f9f9; }
        .filter-container { 
            background: #fff; padding: 16px; border-radius: 6px; 
            border: 1px solid #ddd; margin-bottom: 20px; display: flex; gap: 20px; 
        }
        .filter-group { display: flex; flex-direction: column; }
        select { 
            padding: 8px 12px; width: 250px; font-size: 0.95em; 
            border: 1px solid #ccc; border-radius: 4px; margin-top: 6px;
        }
        .error { color: #b00020; margin-top: 8px; font-weight: bold; }
        .card { border: 1px solid #ddd; padding: 16px; border-radius: 4px; background: #fff; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
        table { width: 100%; border-collapse: collapse; margin-top: 12px; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background: #f1f1f1; }
        .muted { color: #666; font-size: 0.9em; }
        .loading { color: #0066cc; font-weight: bold; margin-bottom: 12px; }
        
        /* Badges de couleur basés STRICTEMENT sur la valeur de alertDto.alerte */
        .badge { padding: 6px 12px; border-radius: 4px; font-weight: bold; font-size: 0.9em; display: inline-block; text-transform: capitalize; }
        
        .rouge { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .orange { background-color: #ffe8cc; color: #d94100; border: 1px solid #ffd8a8; }
        .vert { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .bleu { background-color: #cce5ff; color: #004085; border: 1px solid #b8daff; }
    </style>
</head>
<body>

    <h1>Liste globale des alertes</h1>
    <p class="muted">Suivi des dépassements de seuils par étapes séparées.</p>

    <div class="filter-container">
        <div class="filter-group">
            <label for="selectDemande"><strong>Filtrer par Demande :</strong></label>
            <select id="selectDemande">
                <option value="">-- Toutes les demandes --</option>
            </select>
        </div>
        <div class="filter-group">
            <label for="selectCouleur"><strong>Filtrer par Couleur :</strong></label>
            <select id="selectCouleur">
                <option value="">-- Toutes les couleurs --</option>
                <option value="rouge">Rouge</option>
                <option value="orange">Orange</option>
                <option value="vert">Vert</option>
                <option value="bleu">Bleu</option>
            </select>
        </div>
    </div>
    
    <div id="loading" class="loading">Connexion AJAX à l'API Spring Boot...</div>
    <div id="error" class="error"></div>
    <div id="result" class="card" style="display:none;"></div>

    <script>
        let allAlertsData = [];
        const apiUrl = 'http://localhost:8080/forage/api/alerts';

        $(document).ready(function() {
            // Appel AJAX pour charger l'API
            $.ajax({
                url: apiUrl,
                type: 'GET',
                dataType: 'json',
                success: function(data) {
                    allAlertsData = data;
                    $('#loading').hide();
                    $('#result').show();
                    
                    initFilterOptions(allAlertsData);
                    applyFilters();
                },
                error: function() {
                    $('#loading').hide();
                    $('#error').text("Erreur : Impossible de récupérer les données de l'API.");
                }
            });

            // Déclenchement du filtre au changement
            $('#selectDemande, #selectCouleur').on('change', function() {
                applyFilters();
            });
        });

        // Remplissage dynamique du filtre Demande
        function initFilterOptions(data) {
            const demandesSet = new Set();

            $.each(data, function(i, responseObj) {
                if (responseObj.demande) {
                    demandesSet.add(responseObj.demande.reference);
                }
            });

            $('#selectDemande').html('<option value="">-- Toutes les demandes --</option>');
            demandesSet.forEach(function(ref) {
                $('#selectDemande').append(`<option value="${ref}">${ref}</option>`);
            });
        }

        // Application des filtres combinés
        function applyFilters() {
            const selectedDemande = $('#selectDemande').val();
            const selectedCouleur = $('#selectCouleur').val(); // 'rouge', 'orange', 'vert' ou 'bleu'

            const filteredData = allAlertsData.map(function(responseObj) {
                const newObj = $.extend(true, {}, responseObj);
                const matchDemande = !selectedDemande || (newObj.demande && newObj.demande.reference === selectedDemande);
                
                if (!matchDemande) {
                    newObj.affichageAlertes = [];
                    return newObj;
                }

                // Utilise la liste brute de ton API
                const actualAlertsList = newObj.alerts || newObj.alertDtos || [];
                
                // On filtre directement sur la propriété string textuelle alertDto.alerte
                newObj.affichageAlertes = actualAlertsList.filter(function(alertDto) {
                    if (!selectedCouleur) return true; 
                    
                    // Comparaison directe (ex: "rouge" === "rouge")
                    return alertDto.alerte === selectedCouleur;
                });

                return newObj;
            }).filter(function(responseObj) {
                return responseObj.affichageAlertes && responseObj.affichageAlertes.length > 0;
            });

            renderTable(filteredData);
        }

        // Rendu du tableau avec injection de alertDto.alerte pour la classe CSS et le texte
        function renderTable(dataToDisplay) {
            const container = $('#result');
            if (dataToDisplay.length === 0) {
                container.html('<p class="muted">Aucune alerte correspondante.</p>');
                return;
            }

            let html = `
                <table>
                    <thead>
                        <tr>
                            <th>Référence Demande</th>
                            <th>Statut Initial (Départ)</th>
                            <th>Statut Suivant (Arrivée)</th>
                            <th>Couleur</th>
                            <th>Durée Consommée</th>
                            <th>Seuil Limite</th>
                        </tr>
                    </thead>
                    <tbody>
            `;

            $.each(dataToDisplay, function(index, responseObj) {
                const demandeRef = responseObj.demande ? responseObj.demande.reference : 'Inconnue';
                const listAlerts = responseObj.affichageAlertes || []; 

                $.each(listAlerts, function(idx, alertDto) {
                    // Récupère la valeur brute (ex: "rouge") pour l'appliquer comme classe CSS et texte
                    const nomCouleur = alertDto.alerte ? alertDto.alerte : "gris"; 

                    const st1Display = alertDto.statut1 ? alertDto.statut1 : "Non défini";
                    const st2Display = alertDto.statut2 ? alertDto.statut2 : "Non défini";

                    html += `
                        <tr>
                            <td><strong>${demandeRef}</strong></td>
                            <td>${st1Display}</td>
                            <td>${st2Display}</td>
                            <td><span class="badge ${nomCouleur}">${nomCouleur}</span></td>
                            <td><strong style="color: #b00020;">${alertDto.dureeTravaille} min</strong></td>
                            <td>${alertDto.dureeSeuil} min</td>
                        </tr>
                    `;
                });
            });

            html += '</tbody></table>';
            container.html(html);
        }
    </script>
</body>
</html>