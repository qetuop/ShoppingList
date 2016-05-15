package com.qetuop.shoppinglist.pojo;

/**
 * Created by brian on 4/25/16.
 */
public class Store {
    private long id = 0; // PK col id
    private String name = null;

    public Store() {
    }

    public Store(String name) {
        this.name = name;
    }

    public Store(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
