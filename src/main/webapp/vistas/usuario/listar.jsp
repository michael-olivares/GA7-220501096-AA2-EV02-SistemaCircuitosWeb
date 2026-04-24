<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, co.sena.adso.circuitos.modelo.Usuario" %>
<%@ include file="../header.jsp" %>

<div class="contenido">
    <div class="pagina-encabezado">
        <h2>👤 Gestión de Usuarios</h2>
        <a href="${pageContext.request.contextPath}/UsuarioServlet?accion=nuevo"
           class="btn btn-primario">+ Nuevo Usuario</a>
    </div>

    <%
        String mensaje = request.getParameter("mensaje");
        if (mensaje != null) {
            String clase = "alerta-exito", texto = "";
            switch (mensaje) {
                case "creado":      texto = "✅ Usuario creado exitosamente."; break;
                case "actualizado": texto = "✅ Usuario actualizado."; break;
                case "eliminado":   texto = "🗑️ Usuario eliminado."; break;
                case "error":       texto = "❌ Ocurrió un error."; clase = "alerta-error"; break;
            }
    %>
    <div class="alerta <%= clase %>"><%= texto %></div>
    <% } %>

    <%
        List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
        if (usuarios == null || usuarios.isEmpty()) {
    %>
    <div class="vacio"><p>No hay usuarios registrados.</p></div>
    <% } else { %>
    <div class="tabla-contenedor">
        <table class="tabla">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Email</th>
                    <th>Rol</th>
                    <th>Nivel</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
            <% for (Usuario u : usuarios) { %>
                <tr>
                    <td><%= u.getIdUsuario() %></td>
                    <td><strong><%= u.getNombre() %></strong></td>
                    <td><%= u.getEmail() %></td>
                    <td><span class="badge badge-tipo"><%= u.getRol() %></span></td>
                    <td><%= u.getNivelAcceso() %></td>
                    <td>
                        <span class="badge <%=
                            "activo".equals(u.getEstado())   ? "badge-activo" :
                            "inactivo".equals(u.getEstado()) ? "badge-archivado" : "badge-error"
                        %>">
                            <%= u.getEstado() %>
                        </span>
                    </td>
                    <td class="acciones">
                        <a href="${pageContext.request.contextPath}/UsuarioServlet?accion=editar&id=<%= u.getIdUsuario() %>"
                           class="btn btn-sm btn-editar">✏️ Editar</a>
                        <a href="${pageContext.request.contextPath}/UsuarioServlet?accion=eliminar&id=<%= u.getIdUsuario() %>"
                           class="btn btn-sm btn-eliminar"
                           onclick="return confirm('¿Eliminar usuario «<%= u.getNombre() %>»?')">
                           🗑️ Eliminar
                        </a>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
    <p class="total-registros">Total usuarios: <strong><%= usuarios.size() %></strong></p>
    <% } %>
</div>

</body>
</html>
