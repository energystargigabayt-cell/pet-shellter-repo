package net.paulweider.util;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesReader
{
    private static final Properties PROPERTIES = new Properties();
    private static final String PROP_PATH = "net/paulweider/application.properties";

    static
    {
        getProperties();
    }

    private static void getProperties()
    {
        try(var inStream = PropertiesReader.class.getClassLoader().getResourceAsStream(PROP_PATH))
        {
            PROPERTIES.load(inStream);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key)
    {
        return PROPERTIES.getProperty(key);
    }
}
