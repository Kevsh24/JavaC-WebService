# Enano Sprint-1

# Estudiante
Kevin Salas Hernández  - 116680114\
Grupo: 05-10am

# Estructura de los directorios

Dentro de la carpeta Sprint-1, se encuentran tres subdirectorios. Dentro del subdirectorio Data se encuentra un archivo comprimido con el código fuente utilizado para construir la UI utilizando React.js. El subdirectorio NanoServer contiene el servidor de contenido de estático construído con Maven e implementado en Java, y el subdirectorio Router incluye el servidor de servicios, igualmente construido con Maven e implementado en Java.

# Pasos a seguir para la correcta ejecución del proyecto

1. Levantar el servidor de contenido estático\
Posicionarse sobre el directorio NanoServer y ejecutar la instrucción `mvn compile` en la terminal del sistema para construir la aplicación. Para ejecutarla, solamente se debe ejecutar `mvn exec:java`. El servidor estará corriendo por defecto en el puerto 9000.

2. Levantar el servidor de servicios\
Posicionarse sobre el directorio Router y ejecutar en la terminal del sistema lo siguiente: `mvn compile`. Esto compilará el servidor de servicios. Para ejecutar el servidor, se deberá ingresar el comando `mvn exec:java`. Este servidor estará corriendo en el puerto 8080 por defecto.

3. Dirigirse a la url http://localhost:9000/ (en caso de que el servidor esté corriendo en el puerto 9000). Una vez haya ingresado a la dirección anterior, ya podrá proceder a compilar y ejecutar código Java.

# URIs de acceso
Los URIs utilizados son los siguientes:
* Servidor de servicios
    * localhost:8080/about
    * localhost:8080/code
    * localhost:8080/code/:data

* Servidor de contenido estático
    * localhost:9000/

# ¿Cómo cambiar los puertos?
Dentro de la carpeta src\main\resources, tanto del servidor de contenido estático como del servidor de servicios, se encuentra un archivo llamado port.properties. Este archivo contiene una única propiedad de la forma propertyName=propertyValue. Si desea cambiar el puerto utilizado por alguno de los servidores, solo debe cambiar el propertyValue por el puerto deseado. Para que los cambios surgan efecto, solo deberá bajar el servidor, y ejecutarlo nuevamente ya que no es necesario recompilar el proyecto. Esto lo puede realizar ejecutando solamente el comando `mvn exec:java` en la terminal del sistema.
