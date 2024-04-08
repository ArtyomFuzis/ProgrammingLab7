package com.fuzis.proglab.Server.Collection;

import com.fuzis.proglab.DefaultCartoonPersonCharacter;
import com.fuzis.proglab.Exception.DataBaseConnectionFailedException;
import com.fuzis.proglab.Server.ServerExecutionModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

public class CharacterCollectionSQL extends CharacterCollection {

    private Connection con;

    @Override
    public HashMap<String, DefaultCartoonPersonCharacter> getCharacters() {
        return  new HashMap<>();
    }


    public static CharacterCollection getInstance() {
        return getInstance(CharacterCollectionSQL.class);
    }


    @Override
    public synchronized void add(String id, DefaultCartoonPersonCharacter charac) {

    }

    @Override
    public void load() {
        Properties db_prop = new Properties();
        try {
            db_prop.load(Files.newInputStream(Paths.get("db.properties")));
        } catch (IOException e) {
            throw new DataBaseConnectionFailedException("db.properties not found or could not be loaded");
        }
        String DB_URL = db_prop.getProperty("DB_URL");

        try {
            con = DriverManager.getConnection(DB_URL,db_prop);
        } catch (SQLException e) {
            throw new DataBaseConnectionFailedException("SQL Connection error: " + e.getMessage());
        }

    }

    @Override
    public synchronized DefaultCartoonPersonCharacter getCharacter(String id) {
        return null;
    }

    @Override
    public synchronized DefaultCartoonPersonCharacter deleteCharacter(String id) {
        return null;
    }

    @Override
    public synchronized void clear() {

    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public synchronized int size() {
        return 0;
    }

    @Override
    public void save() {

    }
}
