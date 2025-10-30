package net.paulweider.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager
{
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";

    public static Connection openCon()
    {
        try
        {
            return DriverManager.getConnection(
                    PropertiesReader.get(URL_KEY),
                    PropertiesReader.get(USERNAME_KEY),
                    PropertiesReader.get(PASSWORD_KEY)
            );
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
