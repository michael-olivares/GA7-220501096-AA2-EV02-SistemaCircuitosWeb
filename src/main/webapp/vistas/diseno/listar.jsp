<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, co.sena.adso.circuitos.modelo.Diseno" %>
<%@ include file="../header.jsp" %>

<div class="contenido">
    <div class="pagina-encabezado">
        <h2>🔌 Gestión de Diseños</h2>
        <a href="${pageContext.request.contextPath}/DisenoServlet?accion=nuevo"
           class="btn btn-primario">+ Nuevo Diseño</a>
    </div>

    <%
        String mensaje = request.getParameter("mensaje");
        if (mensaje != null) {
            String clase = "alerta-exito", texto = "";
            switch (mensaje) {
                case "creado":           texto = "✅ Diseño creado exitosamente."; break;
                case "actualizado":      texto = "✅ Diseño actualizado."; break;
                case "eliminado":        texto = "🗑️ Diseño eliminado."; break;
                case "estadoActualizado":texto = "✅ Estado actualizado."; break;
                case "error":            texto = "❌ Ocurrió un error."; clase = "alerta-error"; break;
            }
    %>
    <div class="alerta <%= clase %>"><%= texto %></div>
    <% } %>

    <%
        List<Diseno> disenos = (List<Diseno>) request.getAttribute("disenos");
        if (disenos == null || disenos.isEmpty()) {
    %>
    <div class="vacio">
        <p>No hay diseños registrados.</p>
        <a href="${pageContext.request.contextPath}/DisenoServlet?accion=nuevo"
           class="btn btn-primario">Crear primer diseño</a>
    </div>
    <% } else { %>
    <div class="tabla-contenedor">
        <table class="tabla">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Tipo</th>
                    <th>Estado</th>
                    <th>Versión</th>
                    <th>Proyecto</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
            <% for (Diseno d : disenos) { %>
                <tr>
                    <td><%= d.getIdDiseno() %></td>
                    <td><strong><%= d.getNombre() %></strong></td>
                    <td><span class="badge badge-tipo"><%= d.getTipo() %></span></td>
                    <td>
                        <span class="badge <%=
                            "aprobado".equals(d.getEstado())  ? "badge-activo"     :
                            "borrador".equals(d.getEstado())  ? "badge-archivado"  :
                            "revision".equals(d.getEstado())  ? "badge-finalizado" : "badge-error"
                        %>">
                            <%= d.getEstado() %>
                        </span>
                    </td>
                    <td><%= d.getVersion() %></td>
                    <td>#<%= d.getIdProyecto() %></td>
                    <td class="acciones">
                        <a href="${pageContext.request.contextPath}/DisenoServlet?accion=editar&id=<%= d.getIdDiseno() %>"
                           class="btn btn-sm btn-editar">✏️ Editar</a>
                        <a href="${pageContext.request.contextPath}/DisenoServlet?accion=eliminar&id=<%= d.getIdDiseno() %>"
                           class="btn btn-sm btn-eliminar"
                           onclick="return confirm('¿Eliminar el diseño «<%= d.getNombre() %>»?')">
                           🗑️ Eliminar
                        </a>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
    <p class="total-registros">Total diseños: <strong><%= disenos.size() %></strong></p>
    <% } %>
</div>

</body>
</html>
