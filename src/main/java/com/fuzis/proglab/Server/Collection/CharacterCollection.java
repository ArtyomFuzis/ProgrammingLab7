package com.fuzis.proglab.Server.Collection;

import com.fuzis.proglab.DefaultCartoonPersonCharacter;
import com.fuzis.proglab.Exception.DataBaseConnectionFailedException;
import com.fuzis.proglab.Server.ServerExecutionModule;

import java.util.Date;
import java.util.HashMap;

public abstract class CharacterCollection {

    private static CharacterCollection _instance;

    protected static CharacterCollection getInstance(Class<? extends CharacterCollection> c) {
        try {
            if (_instance == null) _instance = c.newInstance();
            return _instance;
        }
        catch (java.lang.InstantiationException | java.lang.IllegalAccessException ex)
        {
            throw new DataBaseConnectionFailedException("There is no default constructor on the " + c.getName() + "collection class");
        }

    }

    public abstract HashMap<String, DefaultCartoonPersonCharacter> getCharacters();

    public void add(DefaultCartoonPersonCharacter charac) {
        add(charac.getName(), charac);
    }

    public abstract void add(String id, DefaultCartoonPersonCharacter charac);

    public abstract void load();

    public abstract DefaultCartoonPersonCharacter getCharacter(String id);

    public abstract DefaultCartoonPersonCharacter deleteCharacter(String id);

    protected final Date init_date;

    public CharacterCollection() {
        init_date = new Date();
        load();
    }

    public abstract void clear();

    public abstract String getInfo();

    public abstract int size();

    public abstract void save();
}
