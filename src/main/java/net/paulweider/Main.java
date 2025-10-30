package net.paulweider;

import lombok.SneakyThrows;
import net.paulweider.util.PGRConnectionManager;

import java.security.KeyStore;
import java.sql.SQLException;

public class Main
{

    private static final String GET_AVAILAIBLE_COMPS = """
        SELECT TOP 100 * FROM availaibleComps;
    """;

    private static final String GET_AVAILABLE_USERS = """
        SELECT FROM public."user" LIMIT 2
    """;

    private static final String CREATE_INFO_TABLE = """
        CREATE TABLE infoPanel(
            info_id int identity,
            info_description nvarchar(20) not null
        )
    """;
    private static final String DROP_INFO_TABLE = """
        DROP TABLE infoPanel;
    """;

    public static void main(String[] args)
    {
//        try(var con = ConnectionManager.openCon(); var statement = con.createStatement())
//        {
////            System.out.println(statement.execute(DROP_INFO_TABLE));
//            System.out.println(statement.execute(CREATE_INFO_TABLE));
//            System.out.println(con.getTransactionIsolation());
//        }
//        catch (SQLException e)
//        {
//            throw new RuntimeException(e);
//        }
//
//        try(var con = ConnectionManager.openCon(); var statement = con.createStatement())
//        {
//            var result = statement.executeQuery(GET_AVAILAIBLE_COMPS);
////            System.out.println(statement.execute(CREATE_INFO_TABLE));
//            System.out.println(result.getMetaData());
////            System.out.println("Current schema: " + con.getSchema());
//            System.out.println(con.getTransactionIsolation());
//        }
//        catch (SQLException e)
//        {
//            throw new RuntimeException(e);
//        }

        try(var con = PGRConnectionManager.openCon(); var statement = con.createStatement())
        {
            var result = statement.executeQuery(GET_AVAILABLE_USERS);

            System.out.println(result);
            System.out.println(con.getTransactionIsolation());
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    @SneakyThrows
    public static KeyStore getKeyStore()
    {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

        ks.load(null, "changeit".toCharArray());

        return ks;
    }
}
