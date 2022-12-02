package org.ics.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

@Component
public class ConfigGetter
{
    public Properties properties = new Properties();

    public ConfigGetter()
    {
        String path = "src/main/resources/application.properties";
        try
        {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(path));
            properties.load(inputStream);

        } catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
