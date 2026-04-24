<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.sena.adso.circuitos.modelo.Usuario" %>
<%@ include file="../header.jsp" %>

<%
    Usuario u   = (Usuario) request.getAttribute("usuario");
    String modo = (String)  request.getAttribute("modo");
    boolean esNuevo = "nuevo".equals(modo);
%>

<div class="contenido">
    <div class="pagina-encabezado">
        <h2><%= esNuevo ? "👤 Nuevo Usuario" : "✏️ Editar Usuario" %></h2>
        <a href="${pageContext.request.contextPath}/UsuarioServlet?accion=listar"
           class="btn btn-secundario">← Volver</a>
    </div>

    <% String error = (String) request.getAttribute("error");
       if (error != null) { %>
    <div class="alerta alerta-error"><%= error %></div>
    <% } %>

    <div class="card">
        <%-- El formulario llama hashearYEnviar() antes de hacer POST --%>
        <form id="formUsuario"
              action="${pageContext.request.contextPath}/UsuarioServlet"
              method="POST" class="formulario" novalidate
              onsubmit="return hashearYEnviar(event)">

            <input type="hidden" name="accion" value="guardar">
            <input type="hidden" name="idUsuario"
                   value="<%= esNuevo ? "" : u.getIdUsuario() %>">

            <div class="form-fila dos-columnas">

                <div class="campo-grupo">
                    <label for="nombre">Nombre completo <span class="requerido">*</span></label>
                    <input type="text" id="nombre" name="nombre"
                           value="<%= esNuevo ? "" : u.getNombre() %>"
                           placeholder="Nombre Apellido" maxlength="100" required>
                </div>

                <div class="campo-grupo">
                    <label for="email">Email <span class="requerido">*</span></label>
                    <input type="email" id="email" name="email"
                           value="<%= esNuevo ? "" : u.getEmail() %>"
                           placeholder="usuario@sena.edu.co" required>
                </div>
            </div>

            <% if (esNuevo) { %>
            <div class="campo-grupo">
                <label for="passPlano">Contraseña <span class="requerido">*</span></label>
                <%-- Este campo NO se envía al servidor; solo se usa para calcular el hash --%>
                <input type="password" id="passPlano" name="passPlano"
                       placeholder="Mínimo 6 caracteres" required autocomplete="new-password">
                <%-- Campo oculto: recibe el hash SHA-256 calculado por JS --%>
                <input type="hidden" id="contrasenaHash" name="contrasenaHash">
                <small class="campo-ayuda">Se almacenará como hash SHA-256 (nunca en texto plano).</small>
            </div>
            <% } %>

            <div class="form-fila tres-columnas">

                <div class="campo-grupo">
                    <label for="rol">Rol <span class="requerido">*</span></label>
                    <select id="rol" name="rol" required>
                        <%
                            String[] roles = {"disenador", "administrador", "colaborador"};
                            for (String r : roles) {
                                boolean sel = !esNuevo && r.equals(u.getRol());
                        %>
                        <option value="<%= r %>" <%= sel ? "selected" : "" %>><%= r %></option>
                        <% } %>
                    </select>
                </div>

                <div class="campo-grupo">
                    <label for="nivelAcceso">Nivel de Acceso</label>
                    <select id="nivelAcceso" name="nivelAcceso">
                        <%
                            String[] niveles = {"basico", "intermedio", "avanzado", "total"};
                            for (String nv : niveles) {
                                boolean sel = !esNuevo && nv.equals(u.getNivelAcceso());
                        %>
                        <option value="<%= nv %>" <%= sel ? "selected" : "" %>><%= nv %></option>
                        <% } %>
                    </select>
                </div>

                <div class="campo-grupo">
                    <label for="estado">Estado</label>
                    <select id="estado" name="estado">
                        <%
                            String[] estados = {"activo", "inactivo", "suspendido"};
                            for (String est : estados) {
                                boolean sel = !esNuevo && est.equals(u.getEstado());
                        %>
                        <option value="<%= est %>" <%= sel ? "selected" : "" %>><%= est %></option>
                        <% } %>
                    </select>
                </div>
            </div>

            <div class="form-acciones">
                <button type="submit" class="btn btn-primario">
                    <%= esNuevo ? "💾 Registrar Usuario" : "💾 Guardar Cambios" %>
                </button>
                <a href="${pageContext.request.contextPath}/UsuarioServlet?accion=listar"
                   class="btn btn-secundario">Cancelar</a>
            </div>

        </form>
    </div>
</div>

<!--
    Script de hashing SHA-256 en el cliente.
    Uso: antes de enviar el formulario se calcula el hash de la contraseña
    y se coloca en el campo oculto 'contrasenaHash', igual que hace el
    LoginServlet en el servidor (MessageDigest SHA-256).
-->
<script>
/**
 * Calcula SHA-256 de un texto y retorna la cadena hexadecimal.
 * Usa la Web Crypto API (disponible en todos los navegadores modernos).
 * @param {string} texto  Texto plano a hashear
 * @returns {Promise<string>} Hash hexadecimal de 64 caracteres
 */
async function sha256Hex(texto) {
    const encoder = new TextEncoder();
    const data    = encoder.encode(texto);
    const hashBuffer = await crypto.subtle.digest('SHA-256', data);
    const hashArray  = Array.from(new Uint8Array(hashBuffer));
    return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
}

/**
 * Intercepta el envío del formulario de usuario:
 *  1. Valida que la contraseña no esté vacía (solo en modo "nuevo").
 *  2. Calcula el hash SHA-256 y lo coloca en #contrasenaHash.
 *  3. Limpia el campo visible antes de enviar (no exponer la clave en la red).
 *  4. Envía el formulario.
 */
async function hashearYEnviar(event) {
    event.preventDefault();

    const passPlano = document.getElementById('passPlano');
    // Si el campo no existe (modo editar) enviamos directamente
    if (!passPlano) {
        event.target.submit();
        return;
    }

    if (passPlano.value.trim().length < 1) {
        alert('La contraseña es obligatoria.');
        passPlano.focus();
        return;
    }

    // Calcular hash y poner en el campo oculto
    const hash = await sha256Hex(passPlano.value);
    document.getElementById('contrasenaHash').value = hash;

    // Limpiar campo visible (la clave NO viaja al servidor)
    passPlano.value = '';
    passPlano.removeAttribute('name'); // asegurar que no se envíe

    // Enviar
    event.target.submit();
}
</script>

</body>
</html>
