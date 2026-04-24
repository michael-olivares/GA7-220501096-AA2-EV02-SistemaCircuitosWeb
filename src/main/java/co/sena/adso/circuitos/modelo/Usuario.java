package co.sena.adso.circuitos.modelo;

/**
 * Entidad USUARIO — adaptada para capa web (AA3-EV02).
 * Incluye soporte para sesión HTTP.
 *
 * GA7-220501096-AA3-EV02
 * Autor: Michael Ronald Olivares Giraldo — SENA Ficha 3118306
 */
public class Usuario {

    private int    idUsuario;
    private String nombre;
    private String email;
    private String contrasenaHash;  // SHA-256 — nunca se expone en logs
    private String rol;             // disenador|administrador|colaborador
    private String nivelAcceso;
    private String estado;          // activo|inactivo|suspendido

    public Usuario() {}

    public Usuario(String nombre, String email, String contrasenaHash,
                   String rol, String nivelAcceso, String estado) {
        this.nombre         = nombre;
        this.email          = email;
        this.contrasenaHash = contrasenaHash;
        this.rol            = rol;
        this.nivelAcceso    = nivelAcceso;
        this.estado         = estado;
    }

    // Getters y Setters
    public int    getIdUsuario()               { return idUsuario; }
    public void   setIdUsuario(int id)         { this.idUsuario = id; }
    public String getNombre()                  { return nombre; }
    public void   setNombre(String n)          { this.nombre = n; }
    public String getEmail()                   { return email; }
    public void   setEmail(String e)           { this.email = e; }
    public String getContrasenaHash()          { return contrasenaHash; }
    public void   setContrasenaHash(String h)  { this.contrasenaHash = h; }
    public String getRol()                     { return rol; }
    public void   setRol(String r)             { this.rol = r; }
    public String getNivelAcceso()             { return nivelAcceso; }
    public void   setNivelAcceso(String n)     { this.nivelAcceso = n; }
    public String getEstado()                  { return estado; }
    public void   setEstado(String e)          { this.estado = e; }

    @Override
    public String toString() {
        return String.format("Usuario{id=%d | nombre='%s' | email='%s' | rol='%s' | estado='%s'}",
                idUsuario, nombre, email, rol, estado);
    }
}
