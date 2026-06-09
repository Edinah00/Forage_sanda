<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Liste des demandes</title>
</head>
<body>
<jsp:include page="navbar.jsp" />
<h1>Liste des demandes</h1>
<table border="1">
    <thead>
        <tr>
            <th>ID</th>
            <th>Référence</th>
            <th>Client</th>
            <th>Commune</th>
            <th>Lieu</th>
            <th>Date & Heure</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${demandes}" var="d">
            <tr>
                <td>${d.id}</td>
                <td><strong>${d.reference}</strong></td>
                <td>${d.client.nom}</td> 
                <td>${d.commune.libelle}</td>
                <td>${d.lieu}</td>
                <td>
                    <fmt:parseDate value="${d.dateDemande}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm" />
                </td>

                <%-- <td>
                    <a href="${pageContext.request.contextPath}/demandes/delete?id=${d.id}" 
                    onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette demande ?');">
                    Supprimer
                    </a>
                </td> --%>
                
                <td>
                    <a href="${pageContext.request.contextPath}/demandes/edit?id=${d.id}">Modifier</a> |
                    <a href="${pageContext.request.contextPath}/demandes/delete?id=${d.id}" 
                    onclick="return confirm('Supprimer ?');" style="color:red;">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<p><a href="${pageContext.request.contextPath}/demandes/nouveau">Nouvelle demande</a></p>
</body>
</html>
