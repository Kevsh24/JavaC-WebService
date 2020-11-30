/*
Universidad Nacional
Paradigmas de programación

Dr. Carlos Loría Sáenz

Estudiante:
Kevin Salas Hernández  - 116680114

Last edit: Oct,2020
Comments: se ecarga de levantar el servidor de contenido estático.
*/

package com.eif400;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import fi.iki.elonen.SimpleWebServer;

public class Server {

    // Default port
    static String PORT = "9000";

    public static void main(String[] args) throws IOException {

        // Load properties file to set port
        try (InputStream input = new FileInputStream("./src/main/resources/port.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            PORT = String.valueOf(prop.get("port"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String[] arguments = {"-p", PORT, "-d", "web"};

        // Run static content web server
        System.out.format("Using port: %s%n", PORT);
        SimpleWebServer.main(arguments);
    }
}
