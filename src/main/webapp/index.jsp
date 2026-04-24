<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.sena.adso.circuitos.modelo.Usuario" %>
<%@ include file="/vistas/header.jsp" %>

<%
    Usuario usr = (Usuario) session.getAttribute("usuarioLogueado");
%>

<div class="contenido">
    <div class="dashboard-bienvenida">
        <h2>⚡ Bienvenido, <%= usr.getNombre() %></h2>
        <p>Sistema de Diseño Integrado de Circuitos — Panel de Control</p>
        <small>SENA — GA7-220501096-AA3-EV02 — Ficha 3118306 — Abril 2026</small>
    </div>

    <!-- Tarjetas de acceso rápido -->
    <div class="dashboard-tarjetas">

        <div class="tarjeta tarjeta-azul">
            <div class="tarjeta-icono">📁</div>
            <div class="tarjeta-info">
                <h3>Proyectos</h3>
                <p>Gestión CRUD de proyectos de circuitos</p>
            </div>
            <a href="${pageContext.request.contextPath}/ProyectoServlet?accion=listar"
               class="tarjeta-link">Ver proyectos →</a>
        </div>

        <div class="tarjeta tarjeta-verde">
            <div class="tarjeta-icono">🔌</div>
            <div class="tarjeta-info">
                <h3>Diseños</h3>
                <p>Esquemáticos, PCB, simulaciones y modelos 3D</p>
            </div>
            <a href="${pageContext.request.contextPath}/DisenoServlet?accion=listar"
               class="tarjeta-link">Ver diseños →</a>
        </div>

        <div class="tarjeta tarjeta-morado">
            <div class="tarjeta-icono">👤</div>
            <div class="tarjeta-info">
                <h3>Usuarios</h3>
                <p>Administración de roles y accesos</p>
            </div>
            <a href="${pageContext.request.contextPath}/UsuarioServlet?accion=listar"
               class="tarjeta-link">Ver usuarios →</a>
        </div>

    </div>

    <!-- Info técnica de la evidencia -->
    <div class="card info-tecnica">
        <h3>📋 Información Técnica — GA7-220501096-AA3-EV02</h3>
        <div class="info-grid">
            <div><strong>Tecnología:</strong> Java EE — Servlets + JSP</div>
            <div><strong>Base de Datos:</strong> MySQL 8.x (JDBC)</div>
            <div><strong>Métodos HTTP:</strong> GET (consultar/eliminar) · POST (crear/actualizar)</div>
            <div><strong>Patrón:</strong> MVC — Modelo (DAO) · Vista (JSP) · Controlador (Servlet)</div>
            <div><strong>Servidor:</strong> Apache Tomcat 10.x</div>
            <div><strong>Sesión:</strong> HttpSession con control de autenticación</div>
        </div>
    </div>

</div>

</body>
</html>
