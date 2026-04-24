<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.sena.adso.circuitos.modelo.Diseno, co.sena.adso.circuitos.modelo.Proyecto, java.util.List" %>
<%@ include file="../header.jsp" %>

<%
    Diseno d    = (Diseno)   request.getAttribute("diseno");
    String modo = (String)   request.getAttribute("modo");
    List<Proyecto> proyectos = (List<Proyecto>) request.getAttribute("proyectos");
    boolean esNuevo = "nuevo".equals(modo);
%>

<div class="contenido">
    <div class="pagina-encabezado">
        <h2><%= esNuevo ? "🔌 Nuevo Diseño" : "✏️ Editar Diseño" %></h2>
        <a href="${pageContext.request.contextPath}/DisenoServlet?accion=listar"
           class="btn btn-secundario">← Volver</a>
    </div>

    <% String error = (String) request.getAttribute("error");
       if (error != null) { %>
    <div class="alerta alerta-error"><%= error %></div>
    <% } %>

    <div class="card">
        <form action="${pageContext.request.contextPath}/DisenoServlet"
              method="POST" class="formulario" novalidate>

            <input type="hidden" name="accion" value="guardar">
            <input type="hidden" name="idDiseno"
                   value="<%= esNuevo ? "" : d.getIdDiseno() %>">

            <div class="form-fila dos-columnas">

                <!-- Nombre del Diseño -->
                <div class="campo-grupo">
                    <label for="nombre">Nombre del Diseño <span class="requerido">*</span></label>
                    <input type="text" id="nombre" name="nombre"
                           value="<%= esNuevo ? "" : d.getNombre() %>"
                           placeholder="Ej: PCB — Placa de potencia v2"
                           maxlength="120" required>
                </div>

                <!-- Versión -->
                <div class="campo-grupo">
                    <label for="version">Versión</label>
                    <input type="text" id="version" name="version"
                           value="<%= esNuevo ? "1.0" : d.getVersion() %>"
                           placeholder="1.0" maxlength="20">
                </div>
            </div>

            <!-- Descripción -->
            <div class="campo-grupo">
                <label for="descripcion">Descripción</label>
                <textarea id="descripcion" name="descripcion" rows="3"
                          placeholder="Descripción técnica del diseño..."><%= esNuevo ? "" : d.getDescripcion() %></textarea>
            </div>

            <div class="form-fila tres-columnas">

                <!-- Tipo de Diseño -->
                <div class="campo-grupo">
                    <label for="tipo">Tipo <span class="requerido">*</span></label>
                    <select id="tipo" name="tipo" required>
                        <option value="">-- Seleccione --</option>
                        <%
                            String[] tipos = {"esquematico", "pcb", "simulacion", "3d"};
                            for (String t : tipos) {
                                boolean sel = !esNuevo && t.equals(d.getTipo());
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
                            String[] estados = {"borrador", "revision", "aprobado", "rechazado"};
                            for (String est : estados) {
                                boolean sel = !esNuevo && est.equals(d.getEstado());
                        %>
                        <option value="<%= est %>" <%= sel ? "selected" : "" %>><%= est %></option>
                        <% } %>
                    </select>
                </div>

                <!-- Proyecto Asociado -->
                <div class="campo-grupo">
                    <label for="idProyecto">Proyecto <span class="requerido">*</span></label>
                    <select id="idProyecto" name="idProyecto" required>
                        <option value="">-- Seleccione proyecto --</option>
                        <%
                            if (proyectos != null) {
                                for (Proyecto proy : proyectos) {
                                    boolean sel = !esNuevo && proy.getIdProyecto() == d.getIdProyecto();
                        %>
                        <option value="<%= proy.getIdProyecto() %>" <%= sel ? "selected" : "" %>>
                            #<%= proy.getIdProyecto() %> — <%= proy.getNombre() %>
                        </option>
                        <% }} %>
                    </select>
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn btn-primario">
                    <%= esNuevo ? "💾 Crear Diseño" : "💾 Guardar Cambios" %>
                </button>
                <a href="${pageContext.request.contextPath}/DisenoServlet?accion=listar"
                   class="btn btn-secundario">Cancelar</a>
            </div>

        </form>
    </div>
</div>

</body>
</html>
