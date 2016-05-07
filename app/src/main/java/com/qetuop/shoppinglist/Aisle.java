package com.qetuop.shoppinglist;

/**
 * Created by brian on 4/25/16.
 */
public class Aisle {
    private long id = 0; // PK col id
    private long storeId = 0;
    private long itemId = 0;

    public Aisle() {
    }

    public Aisle(long storeId, long itemId) {
        this.storeId = storeId;
        this.itemId = itemId;
    }

    public Aisle(long id, long storeId, long itemId) {
        this.id = id;
        this.storeId = storeId;
        this.itemId = itemId;
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
}
