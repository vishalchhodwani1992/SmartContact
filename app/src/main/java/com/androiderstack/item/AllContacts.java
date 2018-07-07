package com.androiderstack.item;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by user on 07-08-2017.
 */
@Entity
public class AllContacts {


    @Id(autoincrement = true)
    private Long id;

    private String name;

    private String number;

    private boolean isDeleteShown;


    @Generated(hash = 1168625496)
    public AllContacts(Long id, String name, String number, boolean isDeleteShown) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.isDeleteShown = isDeleteShown;
    }

    @Generated(hash = 2139337107)
    public AllContacts() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
