<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 — Página no encontrada</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <style>
        .error-page { display:flex; align-items:center; justify-content:center; min-height:100vh; }
        .error-card { background:#fff; border-radius:12px; padding:3rem 2.5rem;
                      text-align:center; box-shadow:0 4px 20px rgba(0,0,0,.12); max-width:440px; }
        .error-code { font-size:5rem; font-weight:800; color:#dee2e6; line-height:1; }
        .error-icon { font-size:3rem; margin-bottom:.5rem; }
        .error-card h2 { margin:.75rem 0 .5rem; font-size:1.4rem; }
        .error-card p  { color:#666; font-size:.92rem; margin-bottom:1.5rem; }
    </style>
</head>
<body class="login-page">
<div class="error-page">
    <div class="error-card">
        <div class="error-icon">🔍</div>
        <div class="error-code">404</div>
        <h2>Página no encontrada</h2>
        <p>El recurso solicitado no existe o fue movido.<br>
           Verifique la URL e intente nuevamente.</p>
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primario">
            ← Volver al Dashboard
        </a>
    </div>
</div>
</body>
</html>
