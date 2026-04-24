/**
 * app.js — Scripts auxiliares del frontend
 * Sistema de Diseño Integrado de Circuitos
 * GA7-220501096-AA3-EV02 — Michael Olivares — SENA Ficha 3118306 — Abril 2026
 */

/* ── Auto-cierre de alertas ─────────────────────────────────── */
(function autoCloseAlertas() {
    document.addEventListener('DOMContentLoaded', function () {
        const alertas = document.querySelectorAll('.alerta-exito, .alerta-info');
        alertas.forEach(function (alerta) {
            setTimeout(function () {
                alerta.style.transition = 'opacity .5s';
                alerta.style.opacity    = '0';
                setTimeout(function () { alerta.remove(); }, 500);
            }, 4000); // desaparece a los 4 segundos
        });
    });
})();

/* ── Confirmación de eliminación ────────────────────────────── */
(function confirmEliminar() {
    document.addEventListener('DOMContentLoaded', function () {
        // Los botones eliminar ya tienen onclick="return confirm(...)" en los JSP.
        // Este bloque sirve como refuerzo por si se usa clase .btn-eliminar sin onclick.
        document.querySelectorAll('.btn-eliminar:not([onclick])').forEach(function (btn) {
            btn.addEventListener('click', function (e) {
                if (!window.confirm('¿Está seguro de eliminar este registro? Esta acción no se puede deshacer.')) {
                    e.preventDefault();
                }
            });
        });
    });
})();

/* ── Marcar enlace activo en el navbar ──────────────────────── */
(function marcarNavActivo() {
    document.addEventListener('DOMContentLoaded', function () {
        const path = window.location.pathname + window.location.search;
        document.querySelectorAll('.nav-link').forEach(function (link) {
            if (path.indexOf(link.getAttribute('href').split('?')[0].split('/').pop()) !== -1) {
                link.style.background = 'rgba(255,255,255,0.22)';
                link.style.color      = '#fff';
            }
        });
    });
})();
