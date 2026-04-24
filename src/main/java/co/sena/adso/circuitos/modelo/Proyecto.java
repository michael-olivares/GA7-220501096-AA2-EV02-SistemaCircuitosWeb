package co.sena.adso.circuitos.modelo;

/**
 * Entidad PROYECTO — adaptada para capa web (AA3-EV02).
 * Reutiliza los atributos definidos en GA7-220501096-AA2-EV01.
 *
 * GA7-220501096-AA3-EV02
 * Autor: Michael Ronald Olivares Giraldo — SENA Ficha 3118306
 */
public class Proyecto {

    private int    idProyecto;
    private String nombre;
    private String descripcion;
    private String tipoCircuito;    // mixto|microcontrolador|asic|fpga|analogico
    private String estado;          // activo|finalizado|archivado
    private String versionActual;
    private int    idUsuarioCreador;

    public Proyecto() {}

    public Proyecto(String nombre, String descripcion, String tipoCircuito,
                    String estado, String versionActual, int idUsuarioCreador) {
        this.nombre           = nombre;
        this.descripcion      = descripcion;
        this.tipoCircuito     = tipoCircuito;
        this.estado           = estado;
        this.versionActual    = versionActual;
        this.idUsuarioCreador = idUsuarioCreador;
    }

    // Getters y Setters
    public int    getIdProyecto()               { return idProyecto; }
    public void   setIdProyecto(int id)         { this.idProyecto = id; }
    public String getNombre()                   { return nombre; }
    public void   setNombre(String n)           { this.nombre = n; }
    public String getDescripcion()              { return descripcion; }
    public void   setDescripcion(String d)      { this.descripcion = d; }
    public String getTipoCircuito()             { return tipoCircuito; }
    public void   setTipoCircuito(String t)     { this.tipoCircuito = t; }
    public String getEstado()                   { return estado; }
    public void   setEstado(String e)           { this.estado = e; }
    public String getVersionActual()            { return versionActual; }
    public void   setVersionActual(String v)    { this.versionActual = v; }
    public int    getIdUsuarioCreador()         { return idUsuarioCreador; }
    public void   setIdUsuarioCreador(int id)   { this.idUsuarioCreador = id; }

    @Override
    public String toString() {
        return String.format("Proyecto{id=%d | nombre='%s' | tipo='%s' | estado='%s' | version='%s'}",
                idProyecto, nombre, tipoCircuito, estado, versionActual);
    }
}
