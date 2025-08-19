package com.example.backend.model;

import com.example.backend.constant.URole;

public class Role {
    private Integer id;

    private URole name;

    public Role() {
    }

    public Role(Integer id, URole name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public URole getName() {
        return name;
    }

    public void setName(URole name) {
        this.name = name;
    }
}
