package com.qetuop.shoppinglist.pojo;

/**
 * Created by brian on 4/23/16.
 */
public class Item {
    private long id = 0; // PK col id
    private String name = null;
    private int selected = 0;
    private int completed = 0;

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, int selected, int completed) {
        this.name = name;
        this.selected = selected;
        this.completed = completed;
    }

    public Item(long id, String name, int selected, int completed) {
        this.id = id;
        this.name = name;
        this.selected = selected;
        this.completed = completed;
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

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
} // Item
