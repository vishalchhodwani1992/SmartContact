package com.androiderstack.item;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by user on 06-08-2017.
 */
@Entity
public class IMPContacts implements Serializable {


    @Id(autoincrement = true)
    long id;

    String name, number;
    boolean isDeleteShown;

    @Generated(hash = 417056266)
    public IMPContacts(long id, String name, String number, boolean isDeleteShown) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.isDeleteShown = isDeleteShown;
    }

    @Generated(hash = 1253426063)
    public IMPContacts() {
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

    public void setIsDeleteShown(boolean isDeleteShown) {
        this.isDeleteShown = isDeleteShown;
    }

    public boolean getIsDeleteShown() {
        return this.isDeleteShown;
    }
}
