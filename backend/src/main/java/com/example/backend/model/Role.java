package com.example.backend.model;

import com.example.backend.constant.URole;
import org.apache.ibatis.type.Alias;

import java.util.Objects;

@Alias("Role")
public class Role extends AbstractMappedEntity {
    private Long id;

    private URole name;

    public Role() {
        super();
    }

    public Role(Role role) {
        super();
        this.id = role.id;
        this.name = role.name;
    }

    public Role(Long id, URole name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public URole getName() {
        return name;
    }

    public void setName(URole name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && name == role.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
