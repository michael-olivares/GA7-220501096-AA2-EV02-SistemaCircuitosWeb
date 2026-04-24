# ⚡ Sistema de Diseño Integrado de Circuitos — Web
## GA7-220501096-AA3-EV02 — Módulos de Software Codificados y Probados (Web)

**Autor:** Michael Ronald Olivares Giraldo  
**SENA — Regional Distrito Capital — Centro Metalmecánico**  
**Ficha:** 3118306 — Tecnología en Análisis y Desarrollo de Software  
**Fase:** 3 — Ejecución | **Instructor:** Ing. Leonardo Moreno Collazos  
**Fecha:** Abril 2026

---

## Descripción

Aplicación web Java EE que implementa los módulos CRUD del **Sistema de Diseño Integrado de Circuitos** utilizando:

- **Servlets** como controladores (patrón MVC)
- **JSP** como capa de vistas con expresiones y scriptlets
- **JDBC** para acceso a MySQL 8.x
- Métodos **HTTP GET y POST** para todas las operaciones
- **HttpSession** para control de autenticación

Evolución de la evidencia anterior (AA2-EV01 — JDBC consola) hacia arquitectura web completa.

---

## Estructura del Proyecto

```
SistemaCircuitosWeb/
├── src/main/java/co/sena/adso/circuitos/
│   ├── conexion/
│   │   └── ConexionBD.java          ← Singleton JDBC
│   ├── modelo/
│   │   ├── Proyecto.java
│   │   ├── Diseno.java
│   │   └── Usuario.java
│   ├── dao/
│   │   ├── ProyectoDAO.java         ← CRUD Proyecto
│   │   ├── DisenoDAO.java           ← CRUD Diseño
│   │   └── UsuarioDAO.java          ← CRUD + autenticar()
│   └── servlet/
│       ├── LoginServlet.java        ← GET login · POST autenticar
│       ├── ProyectoServlet.java     ← GET listar/nuevo/editar/eliminar · POST guardar
│       ├── DisenoServlet.java       ← GET listar/nuevo/editar/eliminar · POST guardar/cambiarEstado
│       └── UsuarioServlet.java      ← GET listar/nuevo/editar/eliminar · POST guardar
├── src/main/webapp/
│   ├── WEB-INF/
│   │   └── web.xml                  ← Descriptor de despliegue (Jakarta EE 5.0)
│   ├── css/
│   │   └── estilos.css              ← Hoja de estilos del sistema
│   ├── vistas/
│   │   ├── header.jsp               ← Incluye navbar + control de sesión
│   │   ├── login.jsp                ← Formulario de autenticación
│   │   ├── error404.jsp             ← Página de error 404
│   │   ├── error500.jsp             ← Página de error 500
│   │   ├── proyecto/
│   │   │   ├── listar.jsp           ← Tabla de proyectos
│   │   │   └── formulario.jsp       ← Crear / Editar proyecto
│   │   ├── diseno/
│   │   │   ├── listar.jsp           ← Tabla de diseños
│   │   │   └── formulario.jsp       ← Crear / Editar diseño
│   │   └── usuario/
│   │       ├── listar.jsp           ← Tabla de usuarios
│   │       └── formulario.jsp       ← Crear / Editar usuario
│   └── index.jsp                    ← Dashboard principal
├── SISTEMA_DISENO_CIRCUITOS_BD.sql  ← Script SQL completo (CREATE + datos prueba)
├── lib/
    └── mysql-connector-j-9.7.0.jar  ← Driver JDBC MySQL
```

---

## Tecnologías y Requisitos

| Herramienta              | Versión          |
|--------------------------|------------------|
| Java JDK                 | 17 o superior    |
| Apache Tomcat            | 10.x (Jakarta EE)|
| MySQL Server             | 8.0 o superior   |
| MySQL Connector/J        | 9.x              |
| IDE                      | IntelliJ / Eclipse|

---

## Configuración y Despliegue

### 1. Base de datos

```sql
-- Ejecutar el script de la evidencia anterior:
source SISTEMA_DISENO_CIRCUITOS_MODELO_FISICO_COMPLETO.sql;
```

Insertar usuarios de prueba:

```sql
INSERT INTO usuario (nombre, email, contrasena_hash, rol, nivel_acceso, estado)
VALUES
('Administrador SENA', 'admin@sena.edu.co',
 SHA2('admin123', 256), 'administrador', 'total', 'activo'),
('Diseñador Prueba', 'disenador@sena.edu.co',
 SHA2('diseno123', 256), 'disenador', 'intermedio', 'activo');
```

### 2. Configurar credenciales JDBC

Editar `ConexionBD.java`:

```java
private static final String URL     = "jdbc:mysql://localhost:3306/sistema_diseno_circuitos?...";
private static final String USUARIO = "root";   // su usuario MySQL
private static final String CLAVE   = "root";   // su contraseña MySQL
```

### 3. Desplegar en Tomcat

**En IntelliJ IDEA:**
1. `Run > Edit Configurations > + > Tomcat Server`
2. Seleccionar `Tomcat 10.x`
3. En pestaña `Deployment`: agregar artifact WAR exploded
4. Click en ▶️ para iniciar

**Acceso:** `http://localhost:8080/SistemaCircuitosWeb`

---

## Flujo HTTP implementado

### GET — Consultas y navegación
```
GET /ProyectoServlet?accion=listar    → Lista todos los proyectos
GET /ProyectoServlet?accion=nuevo     → Muestra formulario vacío
GET /ProyectoServlet?accion=editar&id=3  → Formulario con datos actuales
GET /ProyectoServlet?accion=eliminar&id=3 → Elimina y redirige
```

### POST — Creación y actualización
```
POST /ProyectoServlet  {accion=guardar, idProyecto="",  nombre=..., ...} → INSERT
POST /ProyectoServlet  {accion=guardar, idProyecto="3", nombre=..., ...} → UPDATE
```

### Autenticación
```
GET  /LoginServlet            → Muestra formulario de login
POST /LoginServlet            → Valida credenciales, crea HttpSession
GET  /LoginServlet?accion=logout → Invalida sesión y redirige
```

---

## Módulos y elementos JSP utilizados

### Scriptlets `<% %>`
- Recuperar atributos de request y session
- Iterar listas con for-each
- Condicionales para mensajes de alerta y modo nuevo/editar

### Expresiones `<%= %>`
- Imprimir valores de objetos del modelo
- Concatenar URLs dinámicas

### Directivas `<%@ %>`
- `<%@ page import="..." %>` para importar clases Java
- `<%@ include file="..." %>` para reutilizar el header/navbar

---

## Estándares de Codificación (GA7-220501096-AA1-EV02)

| Elemento      | Convención       | Ejemplo                    |
|---------------|------------------|----------------------------|
| Paquetes      | dominio invertido | `co.sena.adso.circuitos.*` |
| Clases        | PascalCase       | `ProyectoServlet`          |
| Métodos       | camelCase        | `doPost()`, `estaAutenticado()` |
| Constantes SQL| UPPER_SNAKE_CASE | `SQL_INSERTAR`, `SQL_LISTAR` |
| Comentarios   | Javadoc          | `@param`, `@return`        |

---

## Repositorio Git

```
https://github.com/michael-olivares/GA7-220501096-AA3-EV02-SistemaCircuitosWeb
```

Rama principal: `main`  
Rama desarrollo: `develop`  
Commits con Conventional Commits: `feat(servlet): ...`, `feat(jsp): ...`

---

## Referencias

- SENA. (2026). GA7-220501096-AA2-EV01 — Módulos CRUD con JDBC. Olivares Giraldo, M. R.
- Oracle. (2024). *Java Servlet Technology*. https://jakarta.ee/specifications/servlet/
- Oracle. (2024). *JavaServer Pages Specification*. https://jakarta.ee/specifications/pages/
- Apache. (2024). *Apache Tomcat 10 Documentation*. https://tomcat.apache.org/tomcat-10.1-doc/
- Horstmann, C. (2019). *Core Java, Vol. II — Advanced Features* (11th ed.). Prentice Hall.
