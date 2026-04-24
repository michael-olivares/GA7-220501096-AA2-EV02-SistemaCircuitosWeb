package co.sena.adso.circuitos.modelo;

/**
 * Entidad DISEÑO — adaptada para capa web (AA3-EV02).
 *
 * GA7-220501096-AA3-EV02
 * Autor: Michael Ronald Olivares Giraldo — SENA Ficha 3118306
 */
public class Diseno {

    private int    idDiseno;
    private String nombre;
    private String descripcion;
    private String tipo;            // esquematico|pcb|simulacion|3d
    private String estado;          // borrador|revision|aprobado|rechazado
    private int    idProyecto;
    private String version;

    public Diseno() {}

    public Diseno(String nombre, String descripcion, String tipo,
                  String estado, int idProyecto, String version) {
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.tipo        = tipo;
        this.estado      = estado;
        this.idProyecto  = idProyecto;
        this.version     = version;
    }

    // Getters y Setters
    public int    getIdDiseno()             { return idDiseno; }
    public void   setIdDiseno(int id)       { this.idDiseno = id; }
    public String getNombre()               { return nombre; }
    public void   setNombre(String n)       { this.nombre = n; }
    public String getDescripcion()          { return descripcion; }
    public void   setDescripcion(String d)  { this.descripcion = d; }
    public String getTipo()                 { return tipo; }
    public void   setTipo(String t)         { this.tipo = t; }
    public String getEstado()               { return estado; }
    public void   setEstado(String e)       { this.estado = e; }
    public int    getIdProyecto()           { return idProyecto; }
    public void   setIdProyecto(int id)     { this.idProyecto = id; }
    public String getVersion()              { return version; }
    public void   setVersion(String v)      { this.version = v; }

    @Override
    public String toString() {
        return String.format("Diseno{id=%d | nombre='%s' | tipo='%s' | estado='%s' | proyecto=%d}",
                idDiseno, nombre, tipo, estado, idProyecto);
    }
}
