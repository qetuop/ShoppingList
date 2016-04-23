package com.qetuop.shoppinglist;

/**
 * Created by brian on 4/23/16.
 */
public class Item {
    private long id = 0; // PK col id
    private String name = null;

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Item(long id, String name) {
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

} // Item
