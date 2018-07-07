package com.androiderstack.item;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by vishalchhodwani on 30/8/16.
 */
@Entity
public class BlockContacts implements Serializable{

    @Id(autoincrement = true)
    long id;

    String name, number;
    boolean isDeleteShown;


    @Generated(hash = 209258393)
    public BlockContacts(long id, String name, String number, boolean isDeleteShown) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.isDeleteShown = isDeleteShown;
    }

    @Generated(hash = 1279954059)
    public BlockContacts() {
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isDeleteShown() {
        return isDeleteShown;
    }

    public void setDeleteShown(boolean deleteShown) {
        isDeleteShown = deleteShown;
    }

    public boolean getIsDeleteShown() {
        return this.isDeleteShown;
    }

    public void setIsDeleteShown(boolean isDeleteShown) {
        this.isDeleteShown = isDeleteShown;
    }
}
