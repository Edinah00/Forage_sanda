<style>
    .app-navbar {
        align-items: center;
        background: #20242a;
        display: flex;
        gap: 14px;
        margin: -8px -8px 18px;
        padding: 10px 18px;
    }
    .app-navbar a {
        color: #f5f7fa;
        font-family: Arial, sans-serif;
        font-size: 14px;
        text-decoration: none;
    }
    .app-navbar a:hover {
        text-decoration: underline;
    }
    .app-navbar .brand {
        font-weight: bold;
        margin-right: 10px;
    }
</style>
<nav class="app-navbar">
    <a class="brand" href="${pageContext.request.contextPath}/demandes/liste">Forage</a>
    <a href="${pageContext.request.contextPath}/demandes/liste">Demandes</a>
    <a href="${pageContext.request.contextPath}/demandes/nouveau">Nouvelle demande</a>
    <a href="${pageContext.request.contextPath}/devis/nouveau">Devis</a>
    <a href="${pageContext.request.contextPath}/statut-demande/nouveau">Statuts</a>
    <a href="${pageContext.request.contextPath}/statut-demande/edit">Modifier statuts</a>
    <a href="http://localhost/php/alerts_form.php">Alertes PHP</a>
</nav>
