package co.sena.adso.circuitos;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * Main.java — Lanzador de Tomcat embebido para Railway.app
 *
 * Lee las variables de entorno que Railway inyecta automáticamente:
 *   PORT      → puerto del servidor (Railway lo asigna dinámicamente)
 *   MYSQLHOST → host de la base de datos MySQL de Railway
 *   MYSQLPORT → puerto MySQL (normalmente 3306)
 *   MYSQLUSER → usuario MySQL
 *   MYSQLPASSWORD → contraseña MySQL
 *   MYSQLDATABASE → nombre de la base de datos
 *
 * GA7-220501096-AA3-EV02 — Michael Olivares — SENA Ficha 3118306 — Abril 2026
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // ── 1. Puerto dinámico de Railway ─────────────────────────
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        // ── 2. Configurar Tomcat ──────────────────────────────────
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector(); // necesario en Tomcat 10+

        // ── 3. Directorio base temporal ───────────────────────────
        String baseDir = System.getProperty("java.io.tmpdir");
        tomcat.setBaseDir(baseDir);

        // ── 4. Contexto web — apunta a src/main/webapp ───────────
        String webappDir = new File("src/main/webapp").getAbsolutePath();
        Context ctx = tomcat.addWebapp("/SistemaCircuitosWeb", webappDir);

        // ── 5. Registrar clases compiladas ────────────────────────
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(
            resources,
            "/WEB-INF/classes",
            new File("target/classes").getAbsolutePath(),
            "/"
        ));
        ctx.setResources(resources);

        // ── 6. Arrancar ───────────────────────────────────────────
        tomcat.start();
        System.out.println("========================================");
        System.out.println(" SistemaCircuitosWeb arrancado");
        System.out.println(" Puerto : " + port);
        System.out.println(" URL    : http://localhost:" + port + "/SistemaCircuitosWeb");
        System.out.println("========================================");
        tomcat.getServer().await();
    }
}
