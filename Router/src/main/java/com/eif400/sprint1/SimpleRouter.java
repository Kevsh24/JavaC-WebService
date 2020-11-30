/*
Universidad Nacional
Paradigmas de programación

Dr. Carlos Loría Sáenz

Estudiante:
Kevin Salas Hernández  - 116680114

Last edit: Oct,2020
Comments: basado en ejemplo dado por el profesor, permite compilar y ejecutar codigo Java correctamente.
 */
package com.eif400.sprint1;

import java.util.*;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import fi.iki.elonen.router.RouterNanoHTTPD;
import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.SOCKET_READ_TIMEOUT;
import fi.iki.elonen.router.RouterNanoHTTPD.DefaultHandler;
import fi.iki.elonen.router.RouterNanoHTTPD.IndexHandler;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;

public class SimpleRouter extends RouterNanoHTTPD {

    static int PORT = 8080;

    static public class About extends DefaultHandler {

        /* Access to Mysql server */
        Database pool = new Database();

        @Override
        public String getText() {
            try {
                return pool.getData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return "{\"Error\": \"Error en la base de datos!\"}";
            }
        }

        @Override
        public String getMimeType() {
            return "application/json";
        }

        @Override
        public Response.IStatus getStatus() {
            return Response.Status.OK;
        }
    }

    static public class Code extends DefaultHandler {

        @Override
        public String getText() {
            return "Not implemented";
        }

        @Override
        public String getMimeType() {
            return MIME_PLAINTEXT;
        }

        @Override
        public Response.IStatus getStatus() {
            return Response.Status.OK;
        }

        @Override
        public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {

            //To get the code from client
            String code = session.getParameters().get("data").get(0);

            //Class format is requires to compiled!
            code = "package dsrc; import java.util.*; import java.util.stream.*; import java.util.function.*; public class MainClass { public static void main(String[] args){" + (code != null ? code : "") + "}}";

            // To compile and run MainClass
            Map<String, String> result;
            try {
                saveData("MainClass", code);
                result = compileFile("MainClass");
                result = result.containsKey("Success") ? runFile() : result;
            } catch (IOException ex) {
                return sendResponse(Response.Status.BAD_REQUEST, "Internal server error!");
            }

            return result.containsKey("Success") ? sendResponse(result.get("Success")) : sendResponse(result.get("Error"));
        }

        @Override
        public Response post(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {

            //To get the code from client
            final HashMap<String, String> map = new HashMap<String, String>();
            String code;
            try {
                session.parseBody(map);
                code = map.get("postData");
            } catch (ResponseException | IOException error) {
                return sendResponse(Response.Status.BAD_REQUEST, "Internal server error!");
            }

            //Class format is requires to compiled!
            code = "package dsrc; import java.util.*; import java.util.stream.*; import java.util.function.*; public class Custom { Custom(){} " + (code != null ? code : "") + " }";

            //To create new java file
            Map<String, String> result;
            try {
                saveData("Custom", code);
                result = compileFile("Custom");
            } catch (IOException error) {
                return sendResponse(Response.Status.BAD_REQUEST, "Internal server error!");
            }

            return result.containsKey("Success") ? sendResponse(result.get("Success")) : sendResponse(result.get("Error"));
        }

        private Response sendResponse(String message) {
            return newFixedLengthResponse(getStatus(), getMimeType(), message);
        }

        private Response sendResponse(Response.Status status, String message) {
            return newFixedLengthResponse(status, getMimeType(), message);
        }

        private void saveData(String file, String code) throws IOException {
            String path = Paths.get("").toAbsolutePath().toString() + "/src/main/java/com/eif400/sprint1/dsrc/" + file + ".java";
            Files.write(Paths.get(path), code.getBytes());
        }

        private Map<String, String> compileFile(String file) throws IOException {
            String pathFile = Paths.get("").toAbsolutePath().toString() + "/src/main/java/com/eif400/sprint1/dsrc/" + file + ".java";
            return execute("java --enable-preview -cp target/classes com.eif400.sprint1.Compiler " + pathFile);
        }

        private Map<String, String> runFile() throws IOException {
            return execute("java --enable-preview -cp target/classes/com/eif400/sprint1/dsrc/ dsrc.MainClass");
        }

        private Map<String, String> execute(String command) throws IOException {

            Process process = Runtime.getRuntime().exec(command);
            BufferedReader success = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String out = terminalMessage(error);

            return !"".equals(out) ? Collections.singletonMap("Error", out) : Collections.singletonMap("Success", terminalMessage(success));
        }

        private String terminalMessage(BufferedReader message) throws IOException {
            return message.lines().reduce("", (result, x) -> result + x + "\n");
        }

    }

    public SimpleRouter(int port) throws IOException {
        super(port);
        addMappings();
        start(SOCKET_READ_TIMEOUT, false);
        System.out.format("*** Router running on port %d ***%n", port);
    }

    @Override
    public void addMappings() {
        addRoute("/", IndexHandler.class);
        addRoute("/about", About.class);
        addRoute("/code", Code.class);
        addRoute("/code/:data", Code.class);
    }

    //CORS
    private final List<String> ALLOWED_SITES = Arrays.asList("same-site", "same-origin");

    @Override
    public Response serve(IHTTPSession session) {
        var request_headers = session.getHeaders();

        String origin = "none";
        boolean cors_allowed = request_headers != null
                && "cors".equals(request_headers.get("sec-fetch-mode"))
                && ALLOWED_SITES.indexOf(request_headers.get("sec-fetch-site")) >= 0
                && (origin = request_headers.get("origin")) != null;

        Response response = super.serve(session);
        if (cors_allowed) {
            response.addHeader("Access-Control-Allow-Origin", origin);
            //To allow set content-type header from client
            response.addHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST");

        }
        return response;
    }

    public static void main(String[] args) throws IOException {

        // Load properties file to set port
        try (InputStream input = new FileInputStream("./src/main/resources/port.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            PORT = Integer.parseInt(prop.get("port").toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        PORT = args.length == 0 ? PORT : Integer.parseInt(args[0]);
        new SimpleRouter(PORT);
    }
}