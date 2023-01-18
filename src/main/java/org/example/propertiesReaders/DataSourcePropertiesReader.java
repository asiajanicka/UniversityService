package org.example.propertiesReaders;

import java.util.ResourceBundle;

public class DataSourcePropertiesReader {

    private String path;

    public DataSourcePropertiesReader(){
        if(ResourceBundle.getBundle("config").getString("db").equalsIgnoreCase("mysql")){
            path = "mysqlCreds";
        } else {
            throw new IllegalArgumentException("Incorrect value for db param in config.properties file");
        }
    }

    private String getProperties(String key) {
        return ResourceBundle.getBundle(path).getString(key);
    }

    public String getUrl(){
        return getProperties("url");
    }

    public String getUsername(){
        return getProperties("username");
    }

    public String getPassword(){
        return getProperties("password");
    }

    public String getDriver(){
        return getProperties("driver");
    }

}
