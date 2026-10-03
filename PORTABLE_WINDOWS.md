# Waifu Nexus — Windows Portable

Hacé doble clic en `JUGAR_WAIFU_NEXUS.bat`.

El lanzador busca Java instalado en Windows, JAVA_HOME o el runtime que usa Red Hat Java de VS Code. No usa Maven y no descarga archivos ni crea carpetas temporales.

Si la PC no tiene Java, instalá Java 21 y volvé a ejecutar el lanzador. Para una distribución completamente autónoma sin Java instalado, hay que incluir un runtime Java 21 de Windows x64 dentro de `portable/runtime`.
