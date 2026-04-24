<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 — Error interno del servidor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <style>
        .error-page { display:flex; align-items:center; justify-content:center; min-height:100vh; }
        .error-card { background:#fff; border-radius:12px; padding:3rem 2.5rem;
                      text-align:center; box-shadow:0 4px 20px rgba(0,0,0,.12); max-width:480px; }
        .error-code { font-size:5rem; font-weight:800; color:#f8d7da; line-height:1; }
        .error-icon { font-size:3rem; margin-bottom:.5rem; }
        .error-card h2 { margin:.75rem 0 .5rem; font-size:1.4rem; }
        .error-card p  { color:#666; font-size:.92rem; margin-bottom:1.5rem; }
        .detalle-error {
            background:#f8f9fa; border:1px solid #dee2e6; border-radius:6px;
            padding:.75rem 1rem; font-size:.8rem; color:#555; text-align:left;
            margin-bottom:1.5rem; word-break:break-word;
        }
    </style>
</head>
<body class="login-page">
<div class="error-page">
    <div class="error-card">
        <div class="error-icon">⚠️</div>
        <div class="error-code">500</div>
        <h2>Error interno del servidor</h2>
        <p>Ocurrió un error inesperado al procesar su solicitud.<br>
           Por favor contacte al administrador del sistema.</p>
        <%-- Detalle técnico solo en desarrollo --%>
        <%
            Throwable causa = (Throwable) request.getAttribute("javax.servlet.error.exception");
            if (causa == null) causa = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
            if (causa != null) {
        %>
        <div class="detalle-error">
            <strong>Detalle técnico:</strong><br>
            <%= causa.getClass().getSimpleName() %>: <%= causa.getMessage() %>
        </div>
        <% } %>
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primario">
            ← Volver al Dashboard
        </a>
        <a href="javascript:history.back()" class="btn btn-secundario" style="margin-left:.5rem">
            Atrás
        </a>
    </div>
</div>
</body>
</html>
