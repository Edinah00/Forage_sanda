<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>Création de devis</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 6px; }
        .row-actions { white-space: nowrap; }
        .muted { color: #666; font-size: 0.9em; }
    </style>
</head>
<body>
<jsp:include page="navbar.jsp" />
<h1>Création de devis</h1>

<form id="devisForm" action="${pageContext.request.contextPath}/devis/save" method="post">
    <!-- ID caché du devis pour le mode update -->
    <input type="hidden" name="id" value="${devis.id}" />

    <fieldset>
        <legend>Demande</legend>
        <label>Référence de demande</label>
        <select id="demandeRef" name="demandeRef" onblur="loadDemandeInfo()" required>
            <option value="">-- Choisir --</option>
            <c:forEach var="t" items="${demandes}">
               
                <option value="${t.id}" ${t.id == devis.demande.id ? 'selected' : ''}>${t.reference}</option>
            </c:forEach>
        </select>
       
        <input type="hidden" id="demandeId" name="demandeId" value="${devis.demande.id}" />
        <div class="muted" id="demandeMessage"></div> 

        <div>
            <label>Client</label>
            <input type="text" id="demandeClient" value="${devis.demande.client.nom}" readonly />
        </div>
        <div>
            <label>Date</label>
            <input type="text" id="demandeDate" value="${devis.demande.dateDemande}" readonly />
        </div>
        <div>
            <label>Lieu</label>
            <input type="text" id="demandeLieu" value="${devis.demande.lieu}" readonly />
        </div>
        <div>
            <label>Commune</label>
            <input type="text" id="demandeCommune" value="${devis.demande.commune.libelle}" readonly />
        </div>
    </fieldset>

    <fieldset>
        <legend>Type de devis</legend>
        <select name="typeId" required>
            <option value="">-- Choisir --</option>
            <c:forEach var="t" items="${types}">
                <!-- On ajoute la condition 'selected' ici -->
                <option value="${t.id}" ${t.id == devis.typeDevis.id ? 'selected' : ''}>${t.libelle}</option>
            </c:forEach>
        </select>
    </fieldset>

    <fieldset>
        <legend>Date du devis</legend>
        <input type="datetime-local" name="createdAt" value="${devis.createdAtInputValue}" />
        
    </fieldset>

    <fieldset>
        <legend>Observation</legend>
        <textarea name="observations" rows="3" style="width: 100%;">${devis.observations}</textarea>
    </fieldset>

    <fieldset>
        <legend>Détails</legend>
        <table id="detailsTable">
            <thead>
            <tr>
                <th>Libellé</th>
                <th>Quantité</th>
                <th>Prix unitaire</th>
                <th>Montant</th>
                <th class="row-actions">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="detail" items="${devis.details}" varStatus="status">
                <tr>
                    <!-- On ajoute l'ID caché du detail pour éviter de créer des doublons à la sauvegarde -->
                    <input type="hidden" name="details[${status.index}].id" value="${detail.id}" />
                    
                    <!-- On ajoute les attributs 'value' ici -->
                    <td><input type="text" name="details[${status.index}].libelle" value="${detail.libelle}" required /></td>
                    <td><input type="number" step="0.01" name="details[${status.index}].quantite" value="${detail.quantite}" required oninput="updateMontant(this)" /></td>
                    <td><input type="number" step="0.01" name="details[${status.index}].prixUnitaire" value="${detail.prixUnitaire}" required oninput="updateMontant(this)" /></td>
                    <td class="detail-amount">${detail.montantLigne}</td>
                    
                    <td class="row-actions"><button type="button" onclick="removeRow(this)">Supprimer</button></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <button type="button" onclick="addRow()">Ajouter une ligne</button>
    </fieldset>

    <button type="submit">${devis.id > 0 ? 'Modifier' : 'Valider'}</button>
</form>
<table border="1">
    <thead>
        <tr>
            <th>Demande</th>
            <th>Devis</th>
            <th>date</th>
            <th>Observation</th>
            <th>Details</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${list_devis}" var="d">
            <tr>
                <td>${d.demande.reference}</td>
                <td><strong>${d.typeDevis.libelle}</strong></td>
                <td>${d.createdAtDisplayValue}</td> 
                <td>${d.observations}</td>
                <td>
                    <c:forEach var="detail" items="${d.details}" varStatus="status">
                        ${detail.libelle} | ${detail.quantite} | ${detail.prixUnitaire} | ${detail.montantLigne}
                        <c:if test="${!status.last}"><br /></c:if>
                    </c:forEach>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/devis/edit?id=${d.id}">Modifier</a> |
                    <a href="${pageContext.request.contextPath}/devis/delete?id=${d.id}" 
                    onclick="return confirm('Supprimer ?');" style="color:red;">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<script>
    let detailIndex = ${devis.details.size()};

    function addRow() {
        const tbody = document.querySelector('#detailsTable tbody');
        const row = document.createElement('tr');
        const prefix = 'details[' + detailIndex + ']';
        row.innerHTML =
            '<td><input type="text" name="' + prefix + '.libelle" /></td>' +
            '<td><input type="number" step="0.01" name="' + prefix + '.quantite" oninput="updateMontant(this)" /></td>' +
            '<td><input type="number" step="0.01" name="' + prefix + '.prixUnitaire" oninput="updateMontant(this)" /></td>' +
            '<td class="detail-amount">0</td>' +
            '<td class="row-actions"><button type="button" onclick="removeRow(this)">Supprimer</button></td>';
        tbody.appendChild(row);
        detailIndex++;
    }

    function removeRow(btn) {
        const row = btn.closest('tr');
        if (row) row.remove();
    }

    function updateMontant(input) {
        const row = input.closest('tr');
        const quantiteInput = row.querySelector('input[name$=".quantite"]');
        const prixInput = row.querySelector('input[name$=".prixUnitaire"]');
        const amountCell = row.querySelector('.detail-amount');
        const quantite = parseFloat(quantiteInput.value) || 0;
        const prixUnitaire = parseFloat(prixInput.value) || 0;
        amountCell.textContent = (quantite * prixUnitaire).toFixed(2);
    }

    
</script>
<script>
    window.appContextPath = '${pageContext.request.contextPath}';
</script>
<script src="<c:url value='/resources/js/demande.js' />?v=<%= System.currentTimeMillis() %>"></script>
</body>
</html>
