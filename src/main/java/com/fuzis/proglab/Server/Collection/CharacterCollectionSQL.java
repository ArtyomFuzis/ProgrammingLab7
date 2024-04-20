package com.fuzis.proglab.Server.Collection;

import com.fuzis.proglab.Client.ClientExecutionModule;
import com.fuzis.proglab.DefaultCartoonPersonCharacter;
import com.fuzis.proglab.Enums.Opinion;
import com.fuzis.proglab.Enums.Popularity;
import com.fuzis.proglab.Enums.Sex;
import com.fuzis.proglab.Exception.DataBaseConnectionFailedException;
import com.fuzis.proglab.Exception.NoRulesException;
import com.fuzis.proglab.Server.ServerExecutionModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class CharacterCollectionSQL extends CharacterCollection {

    public static Connection con;
    private static HashMap<String, DefaultCartoonPersonCharacter> characters;

    @Override
    public HashMap<String, DefaultCartoonPersonCharacter> getCharacters() {
        return characters;
    }


    public static CharacterCollection getInstance() {
        return getInstance(CharacterCollectionSQL.class);
    }

    PreparedStatement st_add1;
    PreparedStatement st_add2;
    PreparedStatement st_add3;
    PreparedStatement st_add_s;

    String SON(Object a) {
        if (a == null) return null;
        return a.toString();
    }

    @Override
    public synchronized void add(String id, DefaultCartoonPersonCharacter charac, ServerExecutionModule exec) {
        if(exec.auth_data == null)
        {
            exec.error("Not authorized");
            throw new NoRulesException();
        }
        try {
            AuthData authData = exec.auth_data;
            if (st_add1 == null)
                st_add1 = con.prepareStatement("insert into character (string_id,name,sex,quote,height,weight,popularity,age,description,health,isanimecharacter,belongsto) VALUES (?,?,?::\"sex_1\",?,?,?,?::\"popularity_1\",?,?,?,?,?);");
            if (st_add2 == null)
                st_add2 = con.prepareStatement("insert into character_additionalnames (character_id,additionalname) VALUES (?,?);");
            if (st_add3 == null)
                st_add3 = con.prepareStatement("insert into character_opinions (character_id,personto,opinion) VALUES (?,?,?::\"opinion_1\");");
            if (st_add_s == null)
                st_add_s = con.prepareStatement("select * from character where string_id = ?");
            st_add1.setString(1, id);
            st_add1.setString(2, charac.getName());
            st_add1.setString(3, SON(charac.getSex()));
            st_add1.setString(4, charac.getQuote());
            if (charac.getHeight() != null) st_add1.setDouble(5, charac.getHeight());
            else st_add1.setNull(5, Types.FLOAT);
            if (charac.getWeight() != null) st_add1.setDouble(6, charac.getWeight());
            else st_add1.setNull(6, Types.FLOAT);
            st_add1.setString(7, SON(charac.getPopularity()));
            if (charac.getAge() != null) st_add1.setDouble(8, charac.getAge());
            else st_add1.setNull(8, Types.FLOAT);
            st_add1.setString(9, charac.getDescription());
            if (charac.getHealth() != null) st_add1.setInt(10, charac.getHealth());
            else st_add1.setNull(10, Types.INTEGER);
            if (charac.getAnimeCharacter() != null) st_add1.setBoolean(11, charac.getAnimeCharacter());
            else st_add1.setNull(11, Types.BOOLEAN);
            st_add1.setInt(12, authData.id());
            st_add1.execute();
            st_add_s.setString(1, id);
            ResultSet res = st_add_s.executeQuery();
            res.next();
            int int_id = res.getInt("id");
            if (charac.getAdditionalNames() != null) {
                st_add2.setInt(1, int_id);
                for (var el : charac.getAdditionalNames()) {
                    st_add2.setString(2, el);
                    st_add2.execute();
                }
            }
            if (charac.getOpinions() != null) {
                st_add3.setInt(1, int_id);
                for (var el : charac.getOpinions().keySet()) {
                    st_add3.setString(2, el);
                    st_add3.setString(3, charac.getOpinions().get(el).toString());
                    st_add3.execute();
                }
            }
            characters.put(id, charac);
        } catch (SQLException e) {
            exec.error(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void load() {
        characters = new HashMap<>();
        Properties db_prop = new Properties();
        try {
            db_prop.load(Files.newInputStream(Paths.get("db.properties")));
        } catch (IOException e) {
            //throw new DataBaseConnectionFailedException("db.properties not found or could not be loaded");
            System.out.println("ERROR: NO db.properties file");
            System.exit(1);
        }
        String DB_URL = db_prop.getProperty("DB_URL");
        try {
            con = DriverManager.getConnection(DB_URL, db_prop);
            Statement st = con.createStatement();
            var res = st.execute("DO $$\n" +
                    "BEGIN\n" +
                    "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'sex_1') THEN\n" +
                    "        CREATE TYPE SEX_1 AS ENUM ('Male','Female','Unknown','Stone','Anime');\n" +
                    "    END IF;\n" +
                    "\tIF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'popularity_1') THEN\n" +
                    "        CREATE TYPE POPULARITY_1 AS ENUM('Megazero','Verylow','Justlow','Usualone','Notbad','Normal','Good','Perfect','Awesome','Animecharacter');\n" +
                    "    END IF;\n" +
                    "\tIF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'opinion_1') THEN\n" +
                    "\t\tCREATE TYPE OPINION_1 AS ENUM('Positive','Neutral','Negative','Undefined');\n" +
                    "    END IF;\n" +
                    "END\n" +
                    "$$;\n" +
                    "CREATE TABLE IF NOT EXISTS auth(\n" +
                    "id SERIAL PRIMARY KEY,\n" +
                    "username VARCHAR(255) NOT NULL UNIQUE,\n" +
                    "password CHAR(56)\n" +
                    ");\n" +
                    "CREATE TABLE IF NOT EXISTS character(\n" +
                    "id SERIAL PRIMARY KEY,\n" +
                    "string_id TEXT NOT NULL UNIQUE,\n" +
                    "name TEXT,\n" +
                    "sex SEX_1,\n" +
                    "quote TEXT,\n" +
                    "height FLOAT,\n" +
                    "weight FLOAT,\n" +
                    "popularity POPULARITY_1,\n" +
                    "age FLOAT,\n" +
                    "description TEXT,\n" +
                    "health INT,\n" +
                    "isAnimeCharacter BOOLEAN,\n" +
                    "belongsto INT NOT NULL REFERENCES auth(id) ON DELETE CASCADE\n" +
                    ");\n" +
                    "CREATE TABLE IF NOT EXISTS character_additionalnames\n" +
                    "(\n" +
                    "id SERIAL PRIMARY KEY,\n" +
                    "character_id INT NOT NULL REFERENCES character(id) ON DELETE CASCADE,\n" +
                    "additionalname TEXT\n" +
                    ");\n" +
                    "CREATE TABLE IF NOT EXISTS character_opinions\n" +
                    "(\n" +
                    "id SERIAL PRIMARY KEY,\n" +
                    "character_id INT NOT NULL REFERENCES character(id) ON DELETE CASCADE,\n" +
                    "personto TEXT UNIQUE,\n" +
                    "opinion OPINION_1\n" +
                    ");" +
                    "DO $$\n" +
                    "BEGIN\n" +
                    "    IF NOT EXISTS (SELECT 1 FROM auth WHERE username = 'admin' AND id = 1) THEN\n" +
                    "        DELETE FROM auth WHERE id=1; \n" +
                    "        INSERT INTO auth VALUES (1,'admin',''); \n" +
                    "    END IF;\n" +
                    /*"    IF NOT EXISTS (SELECT 1 FROM (select currval('auth_id_seq') as currval) t WHERE currval=1) THEN\n" +
                    "        PERFORM nextval('auth_id_seq');" +
                    "    END IF;\n" +*/
                    "END\n" +
                    "$$;\n");
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
                                rs.getString("popularity") == null ? null : Popularity.valueOf(rs.getString("popularity")),
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
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataBaseConnectionFailedException(e.getMessage());
        }


    }

    @Override
    public synchronized DefaultCartoonPersonCharacter getCharacter(String id) {
        return characters.get(id);
    }

    PreparedStatement st_delete;
    PreparedStatement st_delete_s;

    @Override
    public synchronized DefaultCartoonPersonCharacter deleteCharacter(String id, ServerExecutionModule exec) {
        if(exec.auth_data == null)
        {
            exec.error("Not authorized");
            throw new NoRulesException();
        }
        try {
            if(st_delete == null)st_delete = con.prepareStatement("DELETE FROM character Where string_id = ?");
            if(st_delete_s == null)st_delete_s = con.prepareStatement("SELECT * FROM character WHERE string_id = ?");
            st_delete_s.setString(1, id);
            ResultSet rs =  st_delete_s.executeQuery();
            if(!rs.next())return null;
            if(!Objects.equals(exec.auth_data.name(), "admin") && rs.getInt("belongsto")!=exec.auth_data.id())
            {
                exec.error("No rules to delete this character");
                throw new NoRulesException();
            }
            st_delete.setString(1,id);
            st_delete.executeUpdate();
            var charac = characters.get(id);
            characters.remove(id);
            return charac;

        } catch (SQLException e) {
            exec.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    PreparedStatement st_clear;
    PreparedStatement st_clear_person;

    @Override
    public synchronized void clear(ServerExecutionModule exec) {
        if(exec.auth_data == null)
        {
            exec.error("Not authorized");
            throw new NoRulesException();
        }
        try {
            if (exec.auth_data.name() == "admin") {

                if (st_clear == null)
                    st_clear = con.prepareStatement("DELETE FROM character;");
                exec.feedback("Deleted: " + st_clear.executeUpdate() + " characters");
                characters.clear();

            } else {
                if (st_clear_person == null)
                    st_clear_person = con.prepareStatement("DELETE FROM character WHERE belongsto = ?;");
                st_clear_person.setInt(1, exec.auth_data.id());
                exec.feedback("Deleted: " + st_clear_person.executeUpdate() + " characters");
                load();
            }
        } catch (SQLException e) {
            exec.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getInfo() {
        return "Class: " + this.getClass() + "\n"
                + "Collection Type: " + characters.getClass() + "\n"
                + "Connection Type: " + con.getClass() + "\n"
                + "Count of elements: " + characters.size() + "\n"
                + "Initialization date: " + init_date;
    }

    @Override
    public synchronized int size() {
        return characters.size();
    }

    @Override
    public void save(ServerExecutionModule exec) {
        exec.warn("There is nothing to do");
    }
}
