package co.sena.adso.circuitos.servlet;

import co.sena.adso.circuitos.dao.ProyectoDAO;
import co.sena.adso.circuitos.modelo.Proyecto;
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
 * Servlet para la gestión CRUD de Proyectos vía HTTP.
 *
 * Mapeo de acciones:
 *  GET  ?accion=listar     → lista todos los proyectos
 *  GET  ?accion=nuevo      → muestra formulario de creación
 *  GET  ?accion=editar&id= → muestra formulario de edición
 *  GET  ?accion=eliminar&id= → elimina y redirige
 *  POST ?accion=guardar    → crea o actualiza proyecto
 *
 * GA7-220501096-AA3-EV02
 * Autor: Michael Ronald Olivares Giraldo — SENA Ficha 3118306 — Abril 2026
 */
@WebServlet("/ProyectoServlet")
public class ProyectoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProyectoDAO proyectoDAO = new ProyectoDAO();

    // ── doGet ─────────────────────────────────────────────────────
    /**
     * Maneja solicitudes GET: listar, mostrar formulario de nuevo/editar, eliminar.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión activa
        if (!estaAutenticado(request)) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            switch (accion) {

                case "listar":
                    List<Proyecto> proyectos = proyectoDAO.listar();
                    request.setAttribute("proyectos", proyectos);
                    request.setAttribute("titulo", "Gestión de Proyectos");
                    request.getRequestDispatcher("/vistas/proyecto/listar.jsp")
                           .forward(request, response);
                    break;

                case "nuevo":
                    request.setAttribute("proyecto", new Proyecto());
                    request.setAttribute("modo", "nuevo");
                    request.getRequestDispatcher("/vistas/proyecto/formulario.jsp")
                           .forward(request, response);
                    break;

                case "editar":
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    Proyecto pEditar = proyectoDAO.buscarPorId(idEditar);
                    if (pEditar == null) {
                        request.setAttribute("error", "Proyecto no encontrado.");
                        doGet(request, response);
                        return;
                    }
                    request.setAttribute("proyecto", pEditar);
                    request.setAttribute("modo", "editar");
                    request.getRequestDispatcher("/vistas/proyecto/formulario.jsp")
                           .forward(request, response);
                    break;

                case "eliminar":
                    int idEliminar = Integer.parseInt(request.getParameter("id"));
                    proyectoDAO.eliminar(idEliminar);
                    response.sendRedirect(request.getContextPath()
                            + "/ProyectoServlet?accion=listar&mensaje=eliminado");
                    break;

                default:
                    response.sendRedirect(request.getContextPath()
                            + "/ProyectoServlet?accion=listar");
            }

        } catch (SQLException e) {
            throw new ServletException("Error de base de datos en ProyectoServlet.doGet: "
                                       + e.getMessage(), e);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ProyectoServlet?accion=listar");
        }
    }

    // ── doPost ────────────────────────────────────────────────────
    /**
     * Maneja solicitudes POST: crear o actualizar un proyecto.
     * Los datos provienen del formulario HTML en formulario.jsp.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión activa
        if (!estaAutenticado(request)) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");

        if ("guardar".equals(accion)) {
            try {
                // Leer parámetros del formulario
                String idStr      = request.getParameter("idProyecto");
                String nombre     = request.getParameter("nombre");
                String descripcion= request.getParameter("descripcion");
                String tipo       = request.getParameter("tipoCircuito");
                String estado     = request.getParameter("estado");
                String version    = request.getParameter("versionActual");
                int    idCreador  = obtenerIdUsuario(request);

                // Validación básica del lado servidor
                if (nombre == null || nombre.trim().isEmpty()) {
                    request.setAttribute("error", "El nombre del proyecto es obligatorio.");
                    request.setAttribute("proyecto", construirDesdeForm(request));
                    request.setAttribute("modo", (idStr == null || idStr.isEmpty()) ? "nuevo" : "editar");
                    request.getRequestDispatcher("/vistas/proyecto/formulario.jsp")
                           .forward(request, response);
                    return;
                }

                Proyecto p = new Proyecto();
                p.setNombre          (nombre.trim());
                p.setDescripcion     (descripcion != null ? descripcion.trim() : "");
                p.setTipoCircuito    (tipo);
                p.setEstado          (estado);
                p.setVersionActual   (version != null ? version.trim() : "1.0");
                p.setIdUsuarioCreador(idCreador);

                boolean exito;
                if (idStr == null || idStr.trim().isEmpty()) {
                    // INSERT — proyecto nuevo
                    exito = proyectoDAO.insertar(p);
                    response.sendRedirect(request.getContextPath()
                            + "/ProyectoServlet?accion=listar&mensaje="
                            + (exito ? "creado" : "error"));
                } else {
                    // UPDATE — proyecto existente
                    p.setIdProyecto(Integer.parseInt(idStr));
                    exito = proyectoDAO.actualizar(p);
                    response.sendRedirect(request.getContextPath()
                            + "/ProyectoServlet?accion=listar&mensaje="
                            + (exito ? "actualizado" : "error"));
                }

            } catch (SQLException e) {
                throw new ServletException("Error de BD en ProyectoServlet.doPost: "
                                           + e.getMessage(), e);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/ProyectoServlet?accion=listar");
        }
    }

    // ── Utilidades privadas ───────────────────────────────────────

    /** Verifica si hay una sesión de usuario activa. */
    private boolean estaAutenticado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("usuarioLogueado") != null;
    }

    /** Obtiene el ID del usuario logueado desde la sesión. */
    private int obtenerIdUsuario(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
            if (u != null) return u.getIdUsuario();
        }
        return 1; // default
    }

    /** Construye un objeto Proyecto desde los parámetros del formulario (para reenviar al form en caso de error). */
    private Proyecto construirDesdeForm(HttpServletRequest request) {
        Proyecto p = new Proyecto();
        String idStr = request.getParameter("idProyecto");
        if (idStr != null && !idStr.isEmpty()) p.setIdProyecto(Integer.parseInt(idStr));
        p.setNombre      (request.getParameter("nombre"));
        p.setDescripcion (request.getParameter("descripcion"));
        p.setTipoCircuito(request.getParameter("tipoCircuito"));
        p.setEstado      (request.getParameter("estado"));
        p.setVersionActual(request.getParameter("versionActual"));
        return p;
    }
}
