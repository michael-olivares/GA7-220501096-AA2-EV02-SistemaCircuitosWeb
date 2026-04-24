package co.sena.adso.circuitos.dao;

import co.sena.adso.circuitos.conexion.ConexionBD;
import co.sena.adso.circuitos.modelo.Proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad PROYECTO.
 * Implementa CRUD completo con PreparedStatement para evitar SQL Injection.
 *
 * Adaptado de GA7-220501096-AA2-EV01 para ser invocado desde Servlets (AA3-EV02).
 *
 * GA7-220501096-AA3-EV02
 * Autor: Michael Ronald Olivares Giraldo — SENA Ficha 3118306 — Abril 2026
 */
public class ProyectoDAO {

    // ── Sentencias SQL ────────────────────────────────────────────
    private static final String SQL_INSERTAR =
        "INSERT INTO proyecto (nombre, descripcion, tipo_circuito, estado, version_actual, id_usuario_creador) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_LISTAR =
        "SELECT id_proyecto, nombre, descripcion, tipo_circuito, estado, version_actual, id_usuario_creador " +
        "FROM proyecto ORDER BY id_proyecto DESC";

    private static final String SQL_BUSCAR_ID =
        "SELECT id_proyecto, nombre, descripcion, tipo_circuito, estado, version_actual, id_usuario_creador " +
        "FROM proyecto WHERE id_proyecto = ?";

    private static final String SQL_ACTUALIZAR =
        "UPDATE proyecto SET nombre=?, descripcion=?, tipo_circuito=?, estado=?, version_actual=? " +
        "WHERE id_proyecto = ?";

    private static final String SQL_ELIMINAR =
        "DELETE FROM proyecto WHERE id_proyecto = ?";

    // ── CREATE ────────────────────────────────────────────────────

    /**
     * Inserta un nuevo proyecto en la base de datos.
     * @param p Objeto Proyecto con los datos a guardar
     * @return true si se insertó correctamente
     * @throws SQLException si ocurre un error de BD
     */
    public boolean insertar(Proyecto p) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getTipoCircuito());
            ps.setString(4, p.getEstado());
            ps.setString(5, p.getVersionActual());
            ps.setInt   (6, p.getIdUsuarioCreador());
            return ps.executeUpdate() > 0;
        }
    }

    // ── READ — Listar todos ───────────────────────────────────────

    /**
     * Retorna todos los proyectos registrados, ordenados por ID descendente.
     * @return Lista de objetos Proyecto
     * @throws SQLException si ocurre un error de BD
     */
    public List<Proyecto> listar() throws SQLException {
        List<Proyecto> lista = new ArrayList<>();
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── READ — Buscar por ID ──────────────────────────────────────

    /**
     * Busca un proyecto por su ID primario.
     * @param id ID del proyecto
     * @return Objeto Proyecto o null si no existe
     * @throws SQLException si ocurre un error de BD
     */
    public Proyecto buscarPorId(int id) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // ── UPDATE ────────────────────────────────────────────────────

    /**
     * Actualiza los datos de un proyecto existente.
     * @param p Objeto Proyecto con los datos actualizados (debe tener idProyecto)
     * @return true si se actualizó al menos una fila
     * @throws SQLException si ocurre un error de BD
     */
    public boolean actualizar(Proyecto p) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getTipoCircuito());
            ps.setString(4, p.getEstado());
            ps.setString(5, p.getVersionActual());
            ps.setInt   (6, p.getIdProyecto());
            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────

    /**
     * Elimina un proyecto por su ID.
     * @param id ID del proyecto a eliminar
     * @return true si se eliminó al menos una fila
     * @throws SQLException si ocurre un error de BD
     */
    public boolean eliminar(int id) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Mapeo ResultSet → Objeto ──────────────────────────────────

    /**
     * Convierte una fila de ResultSet en un objeto Proyecto.
     * @param rs ResultSet posicionado en la fila actual
     * @return Objeto Proyecto poblado
     * @throws SQLException si ocurre un error de acceso a columna
     */
    private Proyecto mapear(ResultSet rs) throws SQLException {
        Proyecto p = new Proyecto();
        p.setIdProyecto       (rs.getInt   ("id_proyecto"));
        p.setNombre           (rs.getString("nombre"));
        p.setDescripcion      (rs.getString("descripcion"));
        p.setTipoCircuito     (rs.getString("tipo_circuito"));
        p.setEstado           (rs.getString("estado"));
        p.setVersionActual    (rs.getString("version_actual"));
        p.setIdUsuarioCreador (rs.getInt   ("id_usuario_creador"));
        return p;
    }
}
