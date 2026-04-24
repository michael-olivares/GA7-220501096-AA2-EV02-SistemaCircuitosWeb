<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.sena.adso.circuitos.modelo.Proyecto" %>
<%@ include file="../header.jsp" %>

<%
    Proyecto p  = (Proyecto) request.getAttribute("proyecto");
    String modo = (String)   request.getAttribute("modo");
    boolean esNuevo = "nuevo".equals(modo);
    String tituloForm = esNuevo ? "Nuevo Proyecto" : "Editar Proyecto";
%>

<div class="contenido">
    <div class="pagina-encabezado">
        <h2><%= esNuevo ? "📁 Nuevo Proyecto" : "✏️ Editar Proyecto" %></h2>
        <a href="${pageContext.request.contextPath}/ProyectoServlet?accion=listar"
           class="btn btn-secundario">← Volver a lista</a>
    </div>

    <%-- Error de validación --%>
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <div class="alerta alerta-error"><%= error %></div>
    <% } %>

    <%--
        Formulario HTML que envía datos al ProyectoServlet mediante método POST.
        - accion=guardar indica al servlet que debe crear o actualizar.
        - idProyecto vacío → INSERT; con valor → UPDATE.
    --%>
    <div class="card">
        <form action="${pageContext.request.contextPath}/ProyectoServlet"
              method="POST"
              class="formulario"
              novalidate>

            <%-- Campo oculto: acción --%>
            <input type="hidden" name="accion" value="guardar">

            <%-- Campo oculto: ID (vacío si es nuevo) --%>
            <input type="hidden" name="idProyecto"
                   value="<%= esNuevo ? "" : p.getIdProyecto() %>">

            <div class="form-fila dos-columnas">

                <!-- Nombre -->
                <div class="campo-grupo">
                    <label for="nombre">Nombre del Proyecto <span class="requerido">*</span></label>
                    <input type="text"
                           id="nombre"
                           name="nombre"
                           value="<%= esNuevo ? "" : p.getNombre() %>"
                           placeholder="Ej: Circuito FPGA — Control Industrial"
                           maxlength="150"
                           required>
                </div>

                <!-- Versión -->
                <div class="campo-grupo">
                    <label for="versionActual">Versión Actual</label>
                    <input type="text"
                           id="versionActual"
                           name="versionActual"
                           value="<%= esNuevo ? "1.0" : p.getVersionActual() %>"
                           placeholder="Ej: 1.0, 2.3"
                           maxlength="20">
                </div>
            </div>

            <!-- Descripción -->
            <div class="campo-grupo">
                <label for="descripcion">Descripción</label>
                <textarea id="descripcion"
                          name="descripcion"
                          rows="3"
                          placeholder="Descripción técnica del proyecto..."><%= esNuevo ? "" : p.getDescripcion() %></textarea>
            </div>

            <div class="form-fila dos-columnas">

                <!-- Tipo de Circuito -->
                <div class="campo-grupo">
                    <label for="tipoCircuito">Tipo de Circuito <span class="requerido">*</span></label>
                    <select id="tipoCircuito" name="tipoCircuito" required>
                        <option value="">-- Seleccione --</option>
                        <%
                            String[] tipos = {"analogico", "microcontrolador", "fpga", "asic", "mixto"};
                            for (String t : tipos) {
                                boolean sel = !esNuevo && t.equals(p.getTipoCircuito());
                        %>
                        <option value="<%= t %>" <%= sel ? "selected" : "" %>><%= t %></option>
                        <% } %>
                    </select>
                </div>

                <!-- Estado -->
                <div class="campo-grupo">
                    <label for="estado">Estado <span class="requerido">*</span></label>
                    <select id="estado" name="estado" required>
                        <%
                            String[] estados = {"activo", "finalizado", "archivado"};
                            for (String est : estados) {
                                boolean sel = !esNuevo && est.equals(p.getEstado());
                        %>
                        <option value="<%= est %>" <%= sel ? "selected" : "" %>><%= est %></option>
                        <% } %>
                    </select>
                </div>
            </div>

            <!-- Botones -->
            <div class="form-acciones">
                <button type="submit" class="btn btn-primario">
                    <%= esNuevo ? "💾 Crear Proyecto" : "💾 Guardar Cambios" %>
                </button>
                <a href="${pageContext.request.contextPath}/ProyectoServlet?accion=listar"
                   class="btn btn-secundario">Cancelar</a>
            </div>

        </form>
    </div>
</div>

</body>
</html>
