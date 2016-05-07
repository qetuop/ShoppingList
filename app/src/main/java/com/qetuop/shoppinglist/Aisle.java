package com.qetuop.shoppinglist;

/**
 * Created by brian on 4/25/16.
 */
public class Aisle {
    private long id = 0; // PK col id
    private long storeId = 0;
    private long itemId = 0;
    private String name = "";

    public Aisle() {
    }

    public Aisle(long storeId, long itemId, String name) {
        this.storeId = storeId;
        this.itemId = itemId;
        this.name = name;
    }

    public Aisle(long id, long storeId, long itemId, String name) {
        this.id = id;
        this.storeId = storeId;
        this.itemId = itemId;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
