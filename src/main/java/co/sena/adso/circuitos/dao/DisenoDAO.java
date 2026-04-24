package co.sena.adso.circuitos.dao;

import co.sena.adso.circuitos.conexion.ConexionBD;
import co.sena.adso.circuitos.modelo.Diseno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad DISEÑO.
 * Implementa CRUD completo con PreparedStatement.
 *
 * GA7-220501096-AA3-EV02
 * Autor: Michael Ronald Olivares Giraldo — SENA Ficha 3118306 — Abril 2026
 */
public class DisenoDAO {

    private static final String SQL_INSERTAR =
        "INSERT INTO diseno (nombre, descripcion, tipo, estado, id_proyecto, version) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_LISTAR =
        "SELECT id_diseno, nombre, descripcion, tipo, estado, id_proyecto, version " +
        "FROM diseno ORDER BY id_diseno DESC";

    private static final String SQL_LISTAR_POR_PROYECTO =
        "SELECT id_diseno, nombre, descripcion, tipo, estado, id_proyecto, version " +
        "FROM diseno WHERE id_proyecto = ? ORDER BY id_diseno DESC";

    private static final String SQL_BUSCAR_ID =
        "SELECT id_diseno, nombre, descripcion, tipo, estado, id_proyecto, version " +
        "FROM diseno WHERE id_diseno = ?";

    private static final String SQL_ACTUALIZAR =
        "UPDATE diseno SET nombre=?, descripcion=?, tipo=?, estado=?, version=? " +
        "WHERE id_diseno = ?";

    private static final String SQL_ACTUALIZAR_ESTADO =
        "UPDATE diseno SET estado=? WHERE id_diseno = ?";

    private static final String SQL_ELIMINAR =
        "DELETE FROM diseno WHERE id_diseno = ?";

    // ── CREATE ────────────────────────────────────────────────────

    /**
     * Inserta un nuevo diseño en la base de datos.
     * @param d Objeto Diseno con los datos a guardar
     * @return true si se insertó correctamente
     */
    public boolean insertar(Diseno d) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getDescripcion());
            ps.setString(3, d.getTipo());
            ps.setString(4, d.getEstado());
            ps.setInt   (5, d.getIdProyecto());
            ps.setString(6, d.getVersion());
            return ps.executeUpdate() > 0;
        }
    }

    // ── READ ──────────────────────────────────────────────────────

    /** Retorna todos los diseños. */
    public List<Diseno> listar() throws SQLException {
        List<Diseno> lista = new ArrayList<>();
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    /** Retorna los diseños de un proyecto específico. */
    public List<Diseno> listarPorProyecto(int idProyecto) throws SQLException {
        List<Diseno> lista = new ArrayList<>();
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_PROYECTO)) {
            ps.setInt(1, idProyecto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /** Busca un diseño por su ID. */
    public Diseno buscarPorId(int id) throws SQLException {
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

    /** Actualiza todos los campos editables de un diseño. */
    public boolean actualizar(Diseno d) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getDescripcion());
            ps.setString(3, d.getTipo());
            ps.setString(4, d.getEstado());
            ps.setString(5, d.getVersion());
            ps.setInt   (6, d.getIdDiseno());
            return ps.executeUpdate() > 0;
        }
    }

    /** Actualiza únicamente el estado de un diseño. */
    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR_ESTADO)) {
            ps.setString(1, nuevoEstado);
            ps.setInt   (2, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────

    /** Elimina un diseño por su ID. */
    public boolean eliminar(int id) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Mapeo ─────────────────────────────────────────────────────
    private Diseno mapear(ResultSet rs) throws SQLException {
        Diseno d = new Diseno();
        d.setIdDiseno    (rs.getInt   ("id_diseno"));
        d.setNombre      (rs.getString("nombre"));
        d.setDescripcion (rs.getString("descripcion"));
        d.setTipo        (rs.getString("tipo"));
        d.setEstado      (rs.getString("estado"));
        d.setIdProyecto  (rs.getInt   ("id_proyecto"));
        d.setVersion     (rs.getString("version"));
        return d;
    }
}
