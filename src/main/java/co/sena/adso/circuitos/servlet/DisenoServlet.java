package co.sena.adso.circuitos.servlet;

import co.sena.adso.circuitos.dao.DisenoDAO;
import co.sena.adso.circuitos.dao.ProyectoDAO;
import co.sena.adso.circuitos.modelo.Diseno;
import co.sena.adso.circuitos.modelo.Proyecto;

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
 * Servlet para la gestión CRUD de Diseños vía HTTP.
 *
 * Mapeo de acciones:
 *  GET  ?accion=listar                   → lista todos los diseños
 *  GET  ?accion=listarPorProyecto&id=    → lista diseños de un proyecto
 *  GET  ?accion=nuevo                    → formulario de creación
 *  GET  ?accion=editar&id=               → formulario de edición
 *  GET  ?accion=eliminar&id=             → eliminar y redirigir
 *  POST ?accion=guardar                  → crear o actualizar diseño
 *  POST ?accion=cambiarEstado            → actualizar solo el estado
 *
 * GA7-220501096-AA3-EV02
 * Autor: Michael Ronald Olivares Giraldo — SENA Ficha 3118306 — Abril 2026
 */
@WebServlet("/DisenoServlet")
public class DisenoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final DisenoDAO   disenoDAO   = new DisenoDAO();
    private final ProyectoDAO proyectoDAO = new ProyectoDAO();

    // ── doGet ─────────────────────────────────────────────────────
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
                    List<Diseno> disenos = disenoDAO.listar();
                    request.setAttribute("disenos", disenos);
                    request.setAttribute("titulo", "Gestión de Diseños");
                    request.getRequestDispatcher("/vistas/diseno/listar.jsp")
                           .forward(request, response);
                    break;

                case "nuevo":
                    List<Proyecto> proyectosNuevo = proyectoDAO.listar();
                    request.setAttribute("proyectos", proyectosNuevo);
                    request.setAttribute("diseno", new Diseno());
                    request.setAttribute("modo", "nuevo");
                    request.getRequestDispatcher("/vistas/diseno/formulario.jsp")
                           .forward(request, response);
                    break;

                case "editar":
                    int idEditar = Integer.parseInt(request.getParameter("id"));
                    Diseno dEditar = disenoDAO.buscarPorId(idEditar);
                    if (dEditar == null) {
                        response.sendRedirect(request.getContextPath()
                                + "/DisenoServlet?accion=listar&error=notfound");
                        return;
                    }
                    List<Proyecto> proyectosEditar = proyectoDAO.listar();
                    request.setAttribute("proyectos", proyectosEditar);
                    request.setAttribute("diseno", dEditar);
                    request.setAttribute("modo", "editar");
                    request.getRequestDispatcher("/vistas/diseno/formulario.jsp")
                           .forward(request, response);
                    break;

                case "eliminar":
                    int idEliminar = Integer.parseInt(request.getParameter("id"));
                    disenoDAO.eliminar(idEliminar);
                    response.sendRedirect(request.getContextPath()
                            + "/DisenoServlet?accion=listar&mensaje=eliminado");
                    break;

                default:
                    response.sendRedirect(request.getContextPath()
                            + "/DisenoServlet?accion=listar");
            }

        } catch (SQLException e) {
            throw new ServletException("Error BD en DisenoServlet.doGet: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/DisenoServlet?accion=listar");
        }
    }

    // ── doPost ────────────────────────────────────────────────────
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
                String idStr      = request.getParameter("idDiseno");
                String nombre     = request.getParameter("nombre");
                String descripcion= request.getParameter("descripcion");
                String tipo       = request.getParameter("tipo");
                String estado     = request.getParameter("estado");
                String version    = request.getParameter("version");
                int    idProyecto = Integer.parseInt(request.getParameter("idProyecto"));

                if (nombre == null || nombre.trim().isEmpty()) {
                    request.setAttribute("error", "El nombre del diseño es obligatorio.");
                    doGet(request, response);
                    return;
                }

                Diseno d = new Diseno();
                d.setNombre      (nombre.trim());
                d.setDescripcion (descripcion != null ? descripcion.trim() : "");
                d.setTipo        (tipo);
                d.setEstado      (estado);
                d.setVersion     (version != null ? version.trim() : "1.0");
                d.setIdProyecto  (idProyecto);

                boolean exito;
                if (idStr == null || idStr.trim().isEmpty()) {
                    exito = disenoDAO.insertar(d);
                    response.sendRedirect(request.getContextPath()
                            + "/DisenoServlet?accion=listar&mensaje="
                            + (exito ? "creado" : "error"));
                } else {
                    d.setIdDiseno(Integer.parseInt(idStr));
                    exito = disenoDAO.actualizar(d);
                    response.sendRedirect(request.getContextPath()
                            + "/DisenoServlet?accion=listar&mensaje="
                            + (exito ? "actualizado" : "error"));
                }

            } else if ("cambiarEstado".equals(accion)) {
                int    id     = Integer.parseInt(request.getParameter("idDiseno"));
                String estado = request.getParameter("estado");
                disenoDAO.actualizarEstado(id, estado);
                response.sendRedirect(request.getContextPath()
                        + "/DisenoServlet?accion=listar&mensaje=estadoActualizado");

            } else {
                response.sendRedirect(request.getContextPath() + "/DisenoServlet?accion=listar");
            }

        } catch (SQLException e) {
            throw new ServletException("Error BD en DisenoServlet.doPost: " + e.getMessage(), e);
        }
    }

    private boolean estaAutenticado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("usuarioLogueado") != null;
    }
}
