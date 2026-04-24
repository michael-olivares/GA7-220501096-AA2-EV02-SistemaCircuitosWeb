<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.sena.adso.circuitos.modelo.Usuario" %>
<%
    // Control de sesión en cada vista protegida
    HttpSession sesionActual = request.getSession(false);
    if (sesionActual == null || sesionActual.getAttribute("usuarioLogueado") == null) {
        response.sendRedirect(request.getContextPath() + "/LoginServlet?mensaje=sesionExpirada");
        return;
    }
    Usuario usuarioActual = (Usuario) sesionActual.getAttribute("usuarioLogueado");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SistemaCircuitos — <%= request.getAttribute("titulo") != null ? request.getAttribute("titulo") : "Panel" %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
    <script src="${pageContext.request.contextPath}/js/app.js" defer></script>
</head>
<body>

<!-- Barra de navegación -->
<nav class="navbar">
    <div class="navbar-brand">
        <span class="navbar-icon">⚡</span>
        <span class="navbar-title">SistemaCircuitos</span>
    </div>
    <ul class="navbar-menu">
        <li><a href="${pageContext.request.contextPath}/index.jsp" class="nav-link">Dashboard</a></li>
        <li><a href="${pageContext.request.contextPath}/ProyectoServlet?accion=listar" class="nav-link">Proyectos</a></li>
        <li><a href="${pageContext.request.contextPath}/DisenoServlet?accion=listar" class="nav-link">Diseños</a></li>
        <li><a href="${pageContext.request.contextPath}/UsuarioServlet?accion=listar" class="nav-link">Usuarios</a></li>
    </ul>
    <div class="navbar-usuario">
        <span class="usuario-nombre">👤 <%= usuarioActual.getNombre() %></span>
        <span class="usuario-rol badge"><%= usuarioActual.getRol() %></span>
        <a href="${pageContext.request.contextPath}/LoginServlet?accion=logout" class="btn btn-logout">Salir</a>
    </div>
</nav>
