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
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet para la gestión CRUD de Usuarios vía HTTP.
 *
 * Acciones:
 *  GET  ?accion=listar        → lista todos los usuarios
 *  GET  ?accion=nuevo         → formulario de registro
 *  GET  ?accion=editar&id=    → formulario de edición
 *  GET  ?accion=eliminar&id=  → eliminar y redirigir
 *  POST ?accion=guardar       → crear o actualizar
 *
 * GA7-220501096-AA3-EV02
 * Autor: Michael Ronald Olivares Giraldo — SENA Ficha 3118306 — Abril 2026
 */
@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!estaAutenticado(request)) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            switch (accion) {

                case "listar":
                    List<Usuario> usuarios = usuarioDAO.listar();
                    request.setAttribute("usuarios", usuarios);
                    request.getRequestDispatcher("/vistas/usuario/listar.jsp")
                           .forward(request, response);
                    break;

                case "nuevo":
                    request.setAttribute("usuario", new Usuario());
                    request.setAttribute("modo", "nuevo");
                    request.getRequestDispatcher("/vistas/usuario/formulario.jsp")
                           .forward(request, response);
                    break;

                case "editar":
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    Usuario uEditar = usuarioDAO.buscarPorId(idEditar);
                    if (uEditar == null) {
                        response.sendRedirect(request.getContextPath()
                                + "/UsuarioServlet?accion=listar");
                        return;
                    }
                    request.setAttribute("usuario", uEditar);
                    request.setAttribute("modo", "editar");
                    request.getRequestDispatcher("/vistas/usuario/formulario.jsp")
                           .forward(request, response);
                    break;

                case "eliminar":
                    int idEliminar = Integer.parseInt(request.getParameter("id"));
                    usuarioDAO.eliminar(idEliminar);
                    response.sendRedirect(request.getContextPath()
                            + "/UsuarioServlet?accion=listar&mensaje=eliminado");
                    break;

                default:
                    response.sendRedirect(request.getContextPath()
                            + "/UsuarioServlet?accion=listar");
            }

        } catch (SQLException e) {
            throw new ServletException("Error BD en UsuarioServlet.doGet: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!estaAutenticado(request)) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        try {
            if ("guardar".equals(accion)) {
                String idStr       = request.getParameter("idUsuario");
                String nombre      = request.getParameter("nombre");
                String email       = request.getParameter("email");
                String hashPass    = request.getParameter("contrasenaHash");
                String rol         = request.getParameter("rol");
                String nivelAcceso = request.getParameter("nivelAcceso");
                String estado      = request.getParameter("estado");

                if (nombre == null || nombre.trim().isEmpty() ||
                    email  == null || email.trim().isEmpty()) {
                    request.setAttribute("error", "Nombre y email son obligatorios.");
                    doGet(request, response);
                    return;
                }

                Usuario u = new Usuario();
                u.setNombre      (nombre.trim());
                u.setEmail       (email.trim());
                u.setContrasenaHash(hashPass != null ? hashPass : "");
                u.setRol         (rol);
                u.setNivelAcceso (nivelAcceso);
                u.setEstado      (estado);

                boolean exito;
                if (idStr == null || idStr.trim().isEmpty()) {
                    exito = usuarioDAO.insertar(u);
                    response.sendRedirect(request.getContextPath()
                            + "/UsuarioServlet?accion=listar&mensaje="
                            + (exito ? "creado" : "error"));
                } else {
                    u.setIdUsuario(Integer.parseInt(idStr));
                    exito = usuarioDAO.actualizar(u);
                    response.sendRedirect(request.getContextPath()
                            + "/UsuarioServlet?accion=listar&mensaje="
                            + (exito ? "actualizado" : "error"));
                }

            } else {
                response.sendRedirect(request.getContextPath() + "/UsuarioServlet?accion=listar");
            }

        } catch (SQLException e) {
            throw new ServletException("Error BD en UsuarioServlet.doPost: " + e.getMessage(), e);
        }
    }

    private boolean estaAutenticado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("usuarioLogueado") != null;
    }
}
