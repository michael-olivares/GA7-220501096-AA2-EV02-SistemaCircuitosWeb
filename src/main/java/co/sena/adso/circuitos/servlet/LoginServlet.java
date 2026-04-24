package co.sena.adso.circuitos.servlet;

import co.sena.adso.circuitos.dao.UsuarioDAO;
import co.sena.adso.circuitos.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Servlet de Autenticación — gestiona inicio y cierre de sesión.
 *
 *  GET  /LoginServlet           → muestra el formulario de login
 *  POST /LoginServlet           → valida credenciales y crea sesión
 *  GET  /LoginServlet?accion=logout → invalida la sesión
 *
 * GA7-220501096-AA3-EV02
 * Autor: Michael Ronald Olivares Giraldo — SENA Ficha 3118306 — Abril 2026
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // ── doGet — muestra login o procesa logout ────────────────────
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("logout".equals(accion)) {
            HttpSession session = request.getSession(false);
            if (session != null) session.invalidate();
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Si ya está autenticado, ir al dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.getRequestDispatcher("/vistas/login.jsp").forward(request, response);
    }

    // ── doPost — valida credenciales y crea sesión ────────────────
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email      = request.getParameter("email");
        String contrasena = request.getParameter("contrasena");

        if (email == null || email.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "Email y contraseña son requeridos.");
            request.getRequestDispatcher("/vistas/login.jsp").forward(request, response);
            return;
        }

        try {
            // Hash SHA-256 de la contraseña ingresada
            String hashIngresado = sha256(contrasena);

            // Autenticar contra la BD
            Usuario usuario = usuarioDAO.autenticar(email.trim(), hashIngresado);

            if (usuario != null) {
                // Crear sesión HTTP
                HttpSession session = request.getSession(true);
                session.setAttribute("usuarioLogueado", usuario);
                session.setAttribute("nombreUsuario", usuario.getNombre());
                session.setAttribute("rolUsuario", usuario.getRol());
                session.setMaxInactiveInterval(30 * 60); // 30 minutos

                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else {
                request.setAttribute("error", "Credenciales incorrectas o usuario inactivo.");
                request.getRequestDispatcher("/vistas/login.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            throw new ServletException("Error BD en LoginServlet: " + e.getMessage(), e);
        }
    }

    /**
     * Calcula el hash SHA-256 de una cadena de texto.
     * @param texto Texto plano a hashear
     * @return Representación hexadecimal del hash
     */
    private String sha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Error al calcular SHA-256: " + e.getMessage(), e);
        }
    }
}
