package com.example.UoA.healthconnect;

/**
 * Created by Nancy on 8/29/14.
 */
public class Dictionary {

    private long id;

    private String type;

    private String value;

    private String name;

    private String description;

    public Dictionary(long id, String type, String value, String name, String description) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.name = name;
        this.description = description;
    }

    public Dictionary(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() { return name; }

    public long getId() { return id; }

}
