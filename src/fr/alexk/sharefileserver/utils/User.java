package fr.alexk.sharefileserver.utils;

import java.util.ArrayList;

public class User {

    private final String name;
    private final String password;


    public User(String name, String password) {
        this.name = name;
        this.password = password;

    }

    public String getName(){
        return name;
    }
    public String getPasswd(){
        return password;
    }

}
