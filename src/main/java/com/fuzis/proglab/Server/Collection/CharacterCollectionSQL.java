package com.fuzis.proglab.Server.Collection;

import com.fuzis.proglab.DefaultCartoonPersonCharacter;
import com.fuzis.proglab.Enums.Opinion;
import com.fuzis.proglab.Enums.Popularity;
import com.fuzis.proglab.Enums.Sex;
import com.fuzis.proglab.Exception.DataBaseConnectionFailedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class CharacterCollectionSQL extends CharacterCollection {

    private Connection con;
    private HashMap<String, DefaultCartoonPersonCharacter> characters;
    private HashMap<String, Integer> belongsto;

    @Override
    public HashMap<String, DefaultCartoonPersonCharacter> getCharacters() {
        return characters;
    }


    public static CharacterCollection getInstance() {
        return getInstance(CharacterCollectionSQL.class);
    }

    PreparedStatement st_add;
    String SON(Object a)
    {
        if(a==null)return null;
        return a.toString();
    }
    @Override
    public synchronized void add(String id, DefaultCartoonPersonCharacter charac,AuthData authData) {
        try {
            if(st_add == null)
             st_add = con.prepareStatement("insert into character (string_id,name,sex,quote,height,weight,popularity,age,description,health,isanimecharacter,belongsto) VALUES (?,?,?::\"sex\",?,?,?,?::\"popularity\",?,?,?,?,?);");
            st_add.setString(1, id);
            st_add.setString(2, charac.getName());
            st_add.setString(3,SON(charac.getSex()));
            st_add.setString(4, charac.getQuote());
            if(charac.getHeight()!=null)st_add.setDouble(5,charac.getHeight()); else st_add.setNull(5, Types.FLOAT);
            if(charac.getWeight()!=null)st_add.setDouble(6,charac.getWeight()); else st_add.setNull(6, Types.FLOAT);
            st_add.setString(7, SON(charac.getPopularity()));
            if(charac.getAge()!=null)st_add.setDouble(8,charac.getAge()); else st_add.setNull(8, Types.FLOAT);
            st_add.setString(9, charac.getDescription());
            if(charac.getHealth()!=null)st_add.setInt(10, charac.getHealth()); else st_add.setNull(10, Types.INTEGER);
            if(charac.getAnimeCharacter()!=null)st_add.setBoolean(11,charac.getAnimeCharacter()); else st_add.setNull(11, Types.BOOLEAN);
            st_add.setInt(12, authData.id());
            st_add.execute();
            characters.put(id, charac);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void load() {
        characters = new HashMap<>();
        belongsto = new HashMap<>();
        Properties db_prop = new Properties();
        try {
            db_prop.load(Files.newInputStream(Paths.get("db.properties")));
        } catch (IOException e) {
            throw new DataBaseConnectionFailedException("db.properties not found or could not be loaded");
        }
        String DB_URL = db_prop.getProperty("DB_URL");
        try {
            con = DriverManager.getConnection(DB_URL, db_prop);
            Statement st = con.createStatement();
            var res = st.execute("DO $$\n" +
                    "BEGIN\n" +
                    "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'sex') THEN\n" +
                    "        CREATE TYPE SEX AS ENUM ('Male','Female','Unknown','Stone','Anime');\n" +
                    "    END IF;\n" +
                    "\tIF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'popularity') THEN\n" +
                    "        CREATE TYPE POPULARITY AS ENUM('Megazero','Verylow','Justlow','Usualone','Notbad','Normal','Good','Perfect','Awesome','Animecharacter');\n" +
                    "    END IF;\n" +
                    "\tIF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'opinion') THEN\n" +
                    "\t\tCREATE TYPE OPINION AS ENUM('Positive','Neutral','Negative','Undefined');\n" +
                    "    END IF;\n" +
                    "END\n" +
                    "$$;\n" +
                    "CREATE TABLE IF NOT EXISTS auth(\n" +
                    "id SERIAL PRIMARY KEY,\n" +
                    "username VARCHAR(255) NOT NULL UNIQUE,\n" +
                    "password CHAR(224)\n" +
                    ");\n" +
                    "CREATE TABLE IF NOT EXISTS character(\n" +
                    "id SERIAL PRIMARY KEY,\n" +
                    "string_id TEXT NOT NULL UNIQUE,\n" +
                    "name TEXT,\n" +
                    "sex SEX,\n" +
                    "quote TEXT,\n" +
                    "height FLOAT,\n" +
                    "weight FLOAT,\n" +
                    "popularity POPULARITY,\n" +
                    "age FLOAT,\n" +
                    "description TEXT,\n" +
                    "health INT,\n" +
                    "isAnimeCharacter BOOLEAN,\n" +
                    "belongsto INT NOT NULL REFERENCES auth(id)\n" +
                    ");\n" +
                    "CREATE TABLE IF NOT EXISTS character_additionalnames\n" +
                    "(\n" +
                    "id SERIAL PRIMARY KEY,\n" +
                    "character_id INT NOT NULL REFERENCES character(id),\n" +
                    "additionalname TEXT\n" +
                    ");\n" +
                    "CREATE TABLE IF NOT EXISTS character_opinions\n" +
                    "(\n" +
                    "id SERIAL PRIMARY KEY,\n" +
                    "character_id INT NOT NULL REFERENCES character(id),\n" +
                    "personto TEXT UNIQUE,\n" +
                    "opinion OPINION\n" +
                    ");");
            ResultSet rs = st.executeQuery("select * from character");
            while (rs.next()) {
                DefaultCartoonPersonCharacter pc =
                        new DefaultCartoonPersonCharacter(rs.getString("name"),
                                rs.getString("sex") == null ? null : Sex.valueOf(rs.getString("sex")),
                                rs.getString("quote"),
                                null,
                                null,
                                rs.getDouble("height"),
                                rs.getDouble("weight"),
                                rs.getBoolean("isAnimeCharacter"),
                                rs.getString("popularity") == null ? null : Popularity.valueOf(rs.getString(rs.getString("popularity"))),
                                rs.getString("description"),
                                rs.getDouble("age"),
                                rs.getInt("health")
                        );
                Statement st2 = con.createStatement();
                ResultSet sub_rs = st2.executeQuery("select * from character_additionalnames where character_id = " + rs.getInt("id") + ";");
                List<String> additional_names = new ArrayList<>();
                while (sub_rs.next()) {
                    additional_names.add(sub_rs.getString("additionalname"));
                }
                pc.setAdditionalNames(additional_names);
                Map<String, Opinion> opinions = new HashMap<>();
                ResultSet sub_rs2 = st2.executeQuery("select * from character_opinions where character_id = " + rs.getInt("id") + ";");
                while (sub_rs2.next()) {
                    opinions.put(
                            sub_rs2.getString("personto"),
                            sub_rs2.getString("opinion") == null ? null : Opinion.valueOf(sub_rs2.getString("opinion")));

                }
                pc.setOpinions(opinions);
                sub_rs2.close();
                characters.put(rs.getString("string_id"), pc);
                belongsto.put(rs.getString("string_id"),rs.getInt("belongsto"));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataBaseConnectionFailedException(e.getMessage());

        }


    }

    @Override
    public synchronized DefaultCartoonPersonCharacter getCharacter(String id) {
        return null;
    }

    @Override
    public synchronized DefaultCartoonPersonCharacter deleteCharacter(String id,AuthData authData) {
        return null;
    }

    @Override
    public synchronized void clear(AuthData authData) {

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
    public void save(AuthData authData) {

    }
}
