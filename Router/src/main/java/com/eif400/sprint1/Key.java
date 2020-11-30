/*
Universidad Nacional
Paradigmas de programación

Dr. Carlos Loría Sáenz

Estudiante:
Kevin Salas Hernández  - 116680114

Last edit: Oct,2020
Comments: configura una conexión a algún servidor Mysql (local o remoto).
*/
package com.eif400.sprint1;

import java.sql.*;

public class Key {

    protected Connection conexion = null;

    public Key() {
    }
    
    protected void connect() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Need to set user and password
        conexion = DriverManager.getConnection("jdbc:mysql://www.simplefit.fit/enano?serverTimezone=UTC","sprint","87654321");
    }
    
    protected void disconnect() throws SQLException {
        if (!conexion.isClosed()) {
            conexion.close();
        }
    }
}
