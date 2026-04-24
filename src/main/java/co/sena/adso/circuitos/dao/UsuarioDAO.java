package co.sena.adso.circuitos.dao;

import co.sena.adso.circuitos.conexion.ConexionBD;
import co.sena.adso.circuitos.modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad USUARIO.
 * Implementa CRUD completo + autenticación web.
 *
 * GA7-220501096-AA3-EV02
 * Autor: Michael Ronald Olivares Giraldo — SENA Ficha 3118306 — Abril 2026
 */
public class UsuarioDAO {

    private static final String SQL_INSERTAR =
        "INSERT INTO usuario (nombre, email, contrasena_hash, rol, nivel_acceso, estado) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_LISTAR =
        "SELECT id_usuario, nombre, email, contrasena_hash, rol, nivel_acceso, estado " +
        "FROM usuario ORDER BY nombre";

    private static final String SQL_BUSCAR_ID =
        "SELECT id_usuario, nombre, email, contrasena_hash, rol, nivel_acceso, estado " +
        "FROM usuario WHERE id_usuario = ?";

    private static final String SQL_BUSCAR_EMAIL =
        "SELECT id_usuario, nombre, email, contrasena_hash, rol, nivel_acceso, estado " +
        "FROM usuario WHERE email = ?";

    private static final String SQL_AUTENTICAR =
        "SELECT id_usuario, nombre, email, rol, nivel_acceso, estado " +
        "FROM usuario WHERE email = ? AND contrasena_hash = ? AND estado = 'activo'";

    private static final String SQL_ACTUALIZAR =
        "UPDATE usuario SET nombre=?, email=?, rol=?, nivel_acceso=?, estado=? " +
        "WHERE id_usuario = ?";

    private static final String SQL_ACTUALIZAR_ESTADO =
        "UPDATE usuario SET estado=? WHERE id_usuario = ?";

    private static final String SQL_ELIMINAR =
        "DELETE FROM usuario WHERE id_usuario = ?";

    // ── CREATE ────────────────────────────────────────────────────

    public boolean insertar(Usuario u) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getContrasenaHash());
            ps.setString(4, u.getRol());
            ps.setString(5, u.getNivelAcceso());
            ps.setString(6, u.getEstado());
            return ps.executeUpdate() > 0;
        }
    }

    // ── READ ──────────────────────────────────────────────────────

    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public Usuario buscarPorEmail(String email) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    /**
     * Autentica un usuario por email y contraseña (hash SHA-256).
     * @param email    Correo del usuario
     * @param hashPass Hash SHA-256 de la contraseña ingresada
     * @return Objeto Usuario si las credenciales son válidas, null si no
     */
    public Usuario autenticar(String email, String hashPass) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_AUTENTICAR)) {
            ps.setString(1, email);
            ps.setString(2, hashPass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario  (rs.getInt   ("id_usuario"));
                    u.setNombre     (rs.getString("nombre"));
                    u.setEmail      (rs.getString("email"));
                    u.setRol        (rs.getString("rol"));
                    u.setNivelAcceso(rs.getString("nivel_acceso"));
                    u.setEstado     (rs.getString("estado"));
                    return u;
                }
            }
        }
        return null;
    }

    // ── UPDATE ────────────────────────────────────────────────────

    public boolean actualizar(Usuario u) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getNivelAcceso());
            ps.setString(5, u.getEstado());
            ps.setInt   (6, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarEstado(int id, String nuevoEstado) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR_ESTADO)) {
            ps.setString(1, nuevoEstado);
            ps.setInt   (2, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────

    public boolean eliminar(int id) throws SQLException {
        Connection con = ConexionBD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Mapeo ─────────────────────────────────────────────────────
    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario  (rs.getInt   ("id_usuario"));
        u.setNombre     (rs.getString("nombre"));
        u.setEmail      (rs.getString("email"));
        u.setContrasenaHash(rs.getString("contrasena_hash"));
        u.setRol        (rs.getString("rol"));
        u.setNivelAcceso(rs.getString("nivel_acceso"));
        u.setEstado     (rs.getString("estado"));
        return u;
    }
}
