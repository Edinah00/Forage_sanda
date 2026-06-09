const contextPath = window.appContextPath || '';

// Variable globale pour stocker les statuts de la demande en cours
let statutsEnCours = [];

// function loadDemandeInfo() {
//     const ref = document.getElementById('demandeRef').value.trim();
//     const msg = document.getElementById('demandeMessage');
//     const selectStatut = document.getElementById('demandeStatutsSelect');
//     const detailsZone = document.getElementById('statutDetailsZone');

//     if (!ref) {
//         msg.textContent = '';
//         if (selectStatut) {
//             selectStatut.innerHTML = '<option value="">-- Choisir un statut --</option>';
//         }
//         if (detailsZone) {
//             detailsZone.style.display = 'none';
//         }
//         return;
//     }

//     const url = contextPath + '/devis/demande-info?reference=' + encodeURIComponent(ref);
//     fetch(url)
//         .then(resp => {
//             if (!resp.ok) throw new Error('Erreur ' + resp.status + ' sur ' + url);
//             return resp.json();
//         })
//         .then(data => {
//             // Remplissage des champs classiques
//             document.getElementById('demandeId').value = data.id;
//             document.getElementById('demandeClient').value = data.client || '';
//             document.getElementById('demandeDate').value = data.dateDemande || '';
//             document.getElementById('demandeLieu').value = data.lieu || '';
//             document.getElementById('demandeCommune').value = data.commune || '';
//             msg.textContent = 'Demande trouvée.';

//             // Sauvegarde de la liste des statuts dans notre variable globale
//             statutsEnCours = data.statuts || [];

//             // Remplissage de la liste déroulante
//             if (selectStatut) {
//                 selectStatut.innerHTML = '<option value="">-- Choisir un statut --</option>';
//                 statutsEnCours.forEach(st => {
//                     const option = document.createElement('option');
//                     option.value = st.id; // L'ID de StatutDemande
//                     option.textContent = st.libelle;
//                     selectStatut.appendChild(option);
//                 });
//             }
            
//             // On cache la zone de détails tant qu'aucun statut n'est choisi
//             if (detailsZone) {
//                 detailsZone.style.display = 'none';
//             }
//         })
//         .catch(err => {
//             msg.textContent = err && err.message ? err.message : 'Demande introuvable.';
//             if (selectStatut) {
//                 selectStatut.innerHTML = '<option value="">-- Choisir un statut --</option>';
//             }
//             if (detailsZone) {
//                 detailsZone.style.display = 'none';
//             }
//         });
// }
function loadDemandeInfo() {
    const selectDemande = document.getElementById('demandeRef');
    const msg = document.getElementById('demandeMessage');
    const selectStatut = document.getElementById('demandeStatutsSelect');
    const detailsZone = document.getElementById('statutDetailsZone');

    // Sécurité au cas où l'élément n'existe pas encore dans le DOM
    if (!selectDemande) return;

    // Récupération de l'ID (value) et de la Référence textuelle (text) de l'option choisie
    const demandeId = selectDemande.value;
    const ref = selectDemande.options[selectDemande.selectedIndex] ? selectDemande.options[selectDemande.selectedIndex].text.trim() : '';

    // Si l'utilisateur a choisi "-- Choisir --" (valeur vide)
    if (!demandeId) {
        if (msg) msg.textContent = '';
        if (selectStatut) {
            selectStatut.innerHTML = '<option value="">-- Choisir un statut --</option>';
        }
        if (detailsZone) {
            detailsZone.style.display = 'none';
        }
        return;
    }

    // On utilise la référence textuelle extraite pour l'API existante
    const url = contextPath + '/devis/demande-info?reference=' + encodeURIComponent(ref);
    
    fetch(url)
        .then(resp => {
            if (!resp.ok) throw new Error('Erreur ' + resp.status + ' sur ' + url);
            return resp.json();
        })
        .then(data => {
            // Remplissage des champs classiques
            const inputId = document.getElementById('demandeId');
            if (inputId) inputId.value = data.id;
            
            document.getElementById('demandeClient').value = data.client || '';
            document.getElementById('demandeDate').value = data.dateDemande || '';
            document.getElementById('demandeLieu').value = data.lieu || '';
            document.getElementById('demandeCommune').value = data.commune || '';
            
            if (msg) msg.textContent = 'Demande trouvée.';

            // Sauvegarde de la liste des statuts dans notre variable globale
            statutsEnCours = data.statuts || [];

            // Remplissage de la liste déroulante des statuts
            if (selectStatut) {
                selectStatut.innerHTML = '<option value="">-- Choisir un statut --</option>';
                statutsEnCours.forEach(st => {
                    const option = document.createElement('option');
                    option.value = st.id; // L'ID de StatutDemande
                    option.textContent = st.libelle;
                    selectStatut.appendChild(option);
                });
            }
            
            if (detailsZone) {
                detailsZone.style.display = 'none';
            }
        })
        .catch(err => {
            if (msg) msg.textContent = err && err.message ? err.message : 'Demande introuvable.';
            if (selectStatut) {
                selectStatut.innerHTML = '<option value="">-- Choisir un statut --</option>';
            }
            if (detailsZone) {
                detailsZone.style.display = 'none';
            }
        });
}
// Cette fonction se déclenche dès qu'on change de statut dans la liste déroulante
function showStatutDetails() {
    const selectStatut = document.getElementById('demandeStatutsSelect');
    const selectedId = selectStatut.value;
    const detailsZone = document.getElementById('statutDetailsZone');

    if (!selectStatut || !detailsZone) {
        return;
    }

    if (!selectedId) {
        detailsZone.style.display = 'none';
        return;
    }

    // On cherche le statut correspondant dans notre variable globale
    const statutTrouve = statutsEnCours.find(st => st.id == selectedId);

    if (statutTrouve) {
        // On affiche la zone d'édition
        detailsZone.style.display = 'block';
        
        // On remplit les champs avec les données actuelles
        document.getElementById('statutDemandeId').value = statutTrouve.id;
        
        // Tronquer le format de date au cas où (ex: 2026-05-27T16:45:00 -> 2026-05-27T16:45)
        if(statutTrouve.dateIso) {
            document.getElementById('statutDate').value = statutTrouve.dateIso.substring(0, 16);
        } else {
            document.getElementById('statutDate').value = '';
        }

        const observations = document.getElementById('observations');
        if (observations) {
            observations.value = statutTrouve.observations || '';
        }
    }
}
