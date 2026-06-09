<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Nouvelle Demande</title>
</head>
<body>
<jsp:include page="navbar.jsp" />
<h1>Nouvelle Demande</h1>
<form action="${pageContext.request.contextPath}/demandes/save" method="post">
    <input type="hidden" name="id" value="${demande.id}" />

    <p>Référence: <strong>${demande.reference != null ? demande.reference : 'Générée automatiquement'}</strong></p>
    <input type="hidden" name="reference" value="${demande.reference}" />

    Lieu: <input type="text" name="lieu" value="${demande.lieu}" required /> <br/>
    
    Date et Heure: 
    <input type="datetime-local" name="dateDemande" value="${demande.dateDemande}" required /> <br/>
    
    Client: 
    <select name="client.id" required>
        <c:forEach items="${clients}" var="c">
            <option value="${c.id}" ${c.id == demande.client.id ? 'selected' : ''}>
                ${c.nom}
            </option>
        </c:forEach>
    </select> <br/>

    Commune: 
    <select name="commune.id" required>
        <c:forEach items="${communes}" var="com">
            <option value="${com.id}" ${com.id == demande.commune.id ? 'selected' : ''}>
                ${com.libelle}
            </option>
        </c:forEach>
    </select> <br/>

    <button type="submit">${demande.id > 0 ? 'Modifier' : 'Enregistrer'}</button>
</form>
<p><a href="${pageContext.request.contextPath}/demandes/liste">Retour à la liste</a></p>
</body>
</html>
