# Waifu Nexus Web

Se agregó una interfaz web independiente sin reemplazar ni modificar la implementación Swing existente.

## Flujo

1. Portada con título y descripción.
2. Fichas de los seis personajes con imagen, historia, anime, elemento y estadísticas.
3. Selección de video Opening y video Ending desde el navegador.
4. Botón **JUGAR AL JUEGO**.
5. Selección de equipos de 1 a 3 personajes.
6. Opening antes de comenzar, si fue cargado.
7. Combate por turnos.
8. Ending automático al terminar, si fue cargado.
9. Pantalla final con resultado y puntaje.
10. Guardado/continuación en `localStorage`.

## Ejecutar

Con Java 17+ y Maven:

```bash
run-web.bat
```

En Linux/macOS:

```bash
./run-web.sh
```

Abrir luego `http://localhost:8080`.

También se puede ejecutar manualmente:

```bash
mvn -DskipTests compile
java -cp target/classes com.waifu.WebAppServer
```

Para usar otro puerto:

```bash
java -cp target/classes com.waifu.WebAppServer 8090
```

## Videos

Los videos no se incluyen en el proyecto. En la portada se pueden seleccionar archivos locales mediante los campos **VIDEO OPENING** y **VIDEO ENDING**. El navegador los reproduce localmente y no los sube a ningún servidor.

La carpeta `src/main/resources/web/videos/` queda preparada para futuros videos incluidos dentro del proyecto.
