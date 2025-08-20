package com.example.backend.repository.Impl;

import com.example.backend.constant.URole;
import com.example.backend.model.Role;
import com.example.backend.repository.RoleRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private final DataSource datasource;


    public RoleRepositoryImpl(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public Optional<Role> findByName(String name) {
        String sql = "SELECT * FROM roles WHERE name = ?";
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, name);
            try (var rs = pre.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getInt("id"));
                    role.setName(URole.valueOf(rs.getString("name")));
                    return Optional.of(role);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
