/*
Universidad Nacional
Paradigmas de programación

Dr. Carlos Loría Sáenz

Estudiante:
Kevin Salas Hernández  - 116680114

Last edit: Oct,2020
Comments: realiza un consulta a la base de datos y le da el formato requerido a la salida.
*/
package com.eif400.sprint1;

import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database extends Key {

    public Database() {
    }

    public String getData() throws SQLException {

        try {
            connect();
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error en las dependencias: driver no encontrado!");
        } catch (SQLException e) {
            throw new SQLException("La base de datos no se encuentra disponible.\n" + e.getMessage());
        }

        CallableStatement stmt = null;
        String result = "";

        try {
            String query = "{CALL getData()}";
            stmt = conexion.prepareCall(query);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result = result + "{\"project\": \"" + rs.getString(1) + "\",\"course\": \"" + rs.getString(2) + "\",\"instance\": \"" + rs.getString(3)
                        + "\",\"cycle\": \"" + rs.getString(4) + "\",\"organization\": \"" + rs.getString(5) + "\",\"projectSite\": \"" + rs.getString(6)
                        + "\",\"team\": { \"code\": \"" + rs.getString(7) + "\", \"members\": [" + getStudents(rs.getString(7)) + "]}}";
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                disconnect();
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }
        return result;
    }

    public String getStudents(String team) throws SQLException {

        try {
            connect();
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error en las dependencias: driver no encontrado!");
        } catch (SQLException e) {
            throw new SQLException("La base de datos no se encuentra disponible.\n" + e.getMessage());
        }

        CallableStatement stmt = null;
        String result = "";

        try {
            String query = "{CALL getTeam(?)}";
            stmt = conexion.prepareCall(query);
            stmt.setString(1, team);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (!"".equals(result)) {
                    result = result + ",";
                }
                result = result + "{\"firstName\": \"" + rs.getString(1) + "\",\"surnames\": \"" + rs.getString(2) + "\",\"id\": \"" + rs.getString(3) + "\"}";
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                disconnect();
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }
        return result;
    }

}
