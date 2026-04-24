<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, co.sena.adso.circuitos.modelo.Proyecto" %>
<%@ include file="../header.jsp" %>

<div class="contenido">
    <div class="pagina-encabezado">
        <h2>📁 Gestión de Proyectos</h2>
        <a href="${pageContext.request.contextPath}/ProyectoServlet?accion=nuevo"
           class="btn btn-primario">
            + Nuevo Proyecto
        </a>
    </div>

    <%-- Alertas de operación --%>
    <%
        String mensaje = request.getParameter("mensaje");
        if (mensaje != null) {
            String clase = "alerta-exito";
            String texto = "";
            switch (mensaje) {
                case "creado":      texto = "✅ Proyecto creado exitosamente."; break;
                case "actualizado": texto = "✅ Proyecto actualizado exitosamente."; break;
                case "eliminado":   texto = "🗑️ Proyecto eliminado exitosamente."; break;
                case "error":       texto = "❌ Ocurrió un error. Intente nuevamente."; clase = "alerta-error"; break;
            }
    %>
    <div class="alerta <%= clase %>"><%= texto %></div>
    <% } %>

    <%-- Tabla de proyectos --%>
    <%
        List<Proyecto> proyectos = (List<Proyecto>) request.getAttribute("proyectos");
        if (proyectos == null || proyectos.isEmpty()) {
    %>
    <div class="vacio">
        <p>No hay proyectos registrados.</p>
        <a href="${pageContext.request.contextPath}/ProyectoServlet?accion=nuevo"
           class="btn btn-primario">Crear primer proyecto</a>
    </div>
    <%
        } else {
    %>
    <div class="tabla-contenedor">
        <table class="tabla">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Tipo de Circuito</th>
                    <th>Estado</th>
                    <th>Versión</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
            <% for (Proyecto p : proyectos) { %>
                <tr>
                    <td><%= p.getIdProyecto() %></td>
                    <td><strong><%= p.getNombre() %></strong></td>
                    <td><span class="badge badge-tipo"><%= p.getTipoCircuito() %></span></td>
                    <td>
                        <span class="badge <%=
                            "activo".equals(p.getEstado())     ? "badge-activo"    :
                            "finalizado".equals(p.getEstado()) ? "badge-finalizado" : "badge-archivado"
                        %>">
                            <%= p.getEstado() %>
                        </span>
                    </td>
                    <td><%= p.getVersionActual() %></td>
                    <td class="acciones">
                        <a href="${pageContext.request.contextPath}/ProyectoServlet?accion=editar&id=<%= p.getIdProyecto() %>"
                           class="btn btn-sm btn-editar">✏️ Editar</a>
                        <a href="${pageContext.request.contextPath}/DisenoServlet?accion=listar&idProyecto=<%= p.getIdProyecto() %>"
                           class="btn btn-sm btn-info">🔌 Diseños</a>
                        <a href="${pageContext.request.contextPath}/ProyectoServlet?accion=eliminar&id=<%= p.getIdProyecto() %>"
                           class="btn btn-sm btn-eliminar"
                           onclick="return confirm('¿Eliminar el proyecto «<%= p.getNombre() %>»? Esta acción no se puede deshacer.')">
                           🗑️ Eliminar
                        </a>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
    <p class="total-registros">Total de proyectos: <strong><%= proyectos.size() %></strong></p>
    <% } %>
</div>

</body>
</html>
