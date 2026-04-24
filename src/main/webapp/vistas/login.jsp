<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — Sistema de Diseño Integrado de Circuitos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body class="login-page">

<div class="login-container">
    <div class="login-card">
        <!-- Logo / Encabezado -->
        <div class="login-header">
            <div class="login-icon">⚡</div>
            <h1>SistemaCircuitos</h1>
            <p>Sistema de Diseño Integrado de Circuitos</p>
            <small>SENA — Ficha 3118306 — GA7-220501096-AA3-EV02</small>
        </div>

        <!-- Mensaje de error si existe -->
        <%
            String error = (String) request.getAttribute("error");
            if (error != null && !error.isEmpty()) {
        %>
        <div class="alerta alerta-error">
            <span>&#9888;</span> <%= error %>
        </div>
        <% } %>

        <!-- Mensaje de parámetro URL -->
        <%
            String msg = request.getParameter("mensaje");
            if ("sesionExpirada".equals(msg)) {
        %>
        <div class="alerta alerta-info">
            <span>&#8505;</span> Su sesión ha expirado. Por favor inicie sesión nuevamente.
        </div>
        <% } %>

        <!-- Formulario de Login — método POST -->
        <form action="${pageContext.request.contextPath}/LoginServlet"
              method="POST" class="login-form" novalidate>

            <div class="campo-grupo">
                <label for="email">Correo electrónico</label>
                <input type="email"
                       id="email"
                       name="email"
                       placeholder="usuario@sena.edu.co"
                       required
                       autocomplete="email">
            </div>

            <div class="campo-grupo">
                <label for="contrasena">Contraseña</label>
                <input type="password"
                       id="contrasena"
                       name="contrasena"
                       placeholder="••••••••"
                       required
                       autocomplete="current-password">
            </div>

            <button type="submit" class="btn btn-primario btn-bloque">
                Iniciar Sesión
            </button>
        </form>

        <!-- Credenciales de prueba -->
        <div class="credenciales-prueba">
            <details>
                <summary>Credenciales de prueba</summary>
                <ul>
                    <li><strong>Admin:</strong> admin@sena.edu.co / admin123</li>
                    <li><strong>Diseñador:</strong> disenador@sena.edu.co / diseno123</li>
                </ul>
            </details>
        </div>
    </div>
</div>

</body>
</html>
