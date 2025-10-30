package net.paulweider.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PGRConnectionManager
{
    private static final String URL_KEY = "db.url2";
    private static final String USERNAME_KEY = "db.user2";
    private static final String PASSWORD_KEY = "db.password2";

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
