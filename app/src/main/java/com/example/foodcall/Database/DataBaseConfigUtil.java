package com.example.foodcall.Database;

import com.example.foodcall.User;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class DataBaseConfigUtil extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[]{User.class};

    public static void main(String[] args) throws IOException, SQLException {
        //writeConfigFile("ormlite_config.txt", classes);
        String ORMLITE_CONFIGURATION_FILE_NAME = "ormlite_config.txt";
        File configFile = new File(new File("").getAbsolutePath()
                .split("app" + File.separator + "build")[0] + File.separator +
                "app" + File.separator +
                "src" + File.separator +
                "main" + File.separator +
                "res" + File.separator +
                "raw" + File.separator +
                ORMLITE_CONFIGURATION_FILE_NAME);
        writeConfigFile(configFile);
    }
}