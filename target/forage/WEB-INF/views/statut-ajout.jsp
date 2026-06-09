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

<form id="devisForm" action="${pageContext.request.contextPath}/statut-demande/save" method="post">
    <fieldset>
        <legend>Demande</legend>
        <label>Référence de demande</label>
        <select id="demandeRef" name="demandeRef" onblur="loadDemandeInfo()" required>
            <option value="">-- Choisir --</option>
            <c:forEach var="t" items="${demandes}">
               
                <option value="${t.id}" ${t.id == devis.demande.id ? 'selected' : ''}>${t.reference}</option>
            </c:forEach>
        </select>
        <input type="hidden" id="demandeId" name="demandeId" value="${demande.id}" />
        <div class="muted" id="demandeMessage"></div> 
        <div>
            <label>Client</label>
            <input type="text" id="demandeClient" readonly />
        </div>
        <div>
            <label>Date</label>
            <input type="text" id="demandeDate" readonly />
        </div>
        <div>
            <label>Lieu</label>
            <input type="text" id="demandeLieu" readonly />
        </div>
        <div>
            <label>Commune</label>
            <input type="text" id="demandeCommune" readonly />
        </div>
    </fieldset>

    <fieldset>
        <legend>statuts</legend>
        <select name="statutId" required>
            <option value="">-- Choisir --</option>
            <c:forEach var="t" items="${statuts}">
                <option value="${t.id}">${t.libelle}</option>
            </c:forEach>
        </select>
        Date et Heure: 
        <input type="datetime-local" name="dateStatut"  required /> <br/>
        Observation: 
        <input type="text" name="observations"  required />
    </fieldset>


    <button type="submit">Valider</button>
</form>
<table border="1">
    <thead>
        <tr>
            <th>Demande</th>
            <th>date</th>
            <th>statut</th>
            <th>obs</th>
            <th>durée travaillée</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${statutDemandes}" var="d">
            <tr>
                <td>${d.demande.reference}</td> 
                <td>${d.dateStatut}</td>
                
                <td><strong>${d.statut.libelle}</strong></td>
                <td><strong>${d.observations}</strong></td>
                <td><strong>${d.dureeTravaille}</strong></td>
                <td>
                    <a href="${pageContext.request.contextPath}/statut-demande/delete?id=${d.id}" 
                    onclick="return confirm('Supprimer ?');" style="color:red;">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<script>
    window.appContextPath = '${pageContext.request.contextPath}';
</script>
<script src="<c:url value='/resources/js/demande.js' />?v=<%= System.currentTimeMillis() %>"></script>
</body>
</html>
