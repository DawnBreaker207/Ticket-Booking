package com.example.backend.repository.Impl;

import com.example.backend.constant.URole;
import com.example.backend.exception.wrapper.UserNotFoundException;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final DataSource datasource;

    public UserRepositoryImpl(DataSource datasource) {
        this.datasource = datasource;
    }

    public User save(User user) {
        String sql = "INSERT INTO user (username, email, password) VALUES ( ?, ? ,? )";
        String sqlRole = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
        try (Connection conn = datasource.getConnection()) {
            PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pre.setString(1, user.getUsername());
            pre.setString(2, user.getEmail());
            pre.setString(3, user.getPassword());
            pre.executeUpdate();


            try (ResultSet result = pre.getGeneratedKeys()) {
                if (result.next()) {
                    user.setId(result.getLong(1));
                }
            }
            var preRole = conn.prepareStatement(sqlRole);
            for (Role r : user.getRoles()) {
                preRole.setLong(1, user.getId());
                preRole.setLong(2, r.getId());
                preRole.addBatch();
            }
            preRole.executeBatch();
            return user;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        String sqlRole = """
                SELECT r.id, r.name
                FROM roles r
                JOIN user_role ur ON r.id = ur.role_id
                WHERE ur.user_id = ?
                """;
        List<User> users = new ArrayList<>();
        try (var conn = datasource.getConnection()) {

            try (var pre = conn.prepareStatement(sql); var result = pre.executeQuery()) {

                while (result.next()) {
                    var user = new User();
                    user.setId(result.getLong("id"));
                    user.setUsername(result.getString("username"));
                    user.setEmail(result.getString("email"));
                    user.setPassword(result.getString("password"));
                    user.setCreatedAt(result.getTimestamp("created_at").toInstant());
                    user.setCreatedAt(result.getTimestamp("updated_at").toInstant());
//                  Load role for each user
                    Set<Role> roles = new HashSet<>();
                    try (var preRole = conn.prepareStatement(sqlRole);) {
                        preRole.setLong(1, user.getId());

                        try (var rsRole = preRole.executeQuery();) {
                            while (rsRole.next()) {
                                Role role = new Role();
                                role.setId(rsRole.getInt("id"));
                                role.setName(URole.valueOf(rsRole.getString("name")));
                                roles.add(role);
                            }

                        }

                    }
                    user.setRoles(roles);
                    users.add(user);
                }
            }
            return users;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<User> findOne(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        String sqlRole = """
                SELECT r.id, r.name
                FROM roles r
                JOIN user_role ur ON r.id = ur.role_id
                WHERE ur.user_id = ?
                """;
        User user = new User();
        try (var conn = datasource.getConnection()) {
            var pre = conn.prepareStatement(sql);
            pre.setLong(1, id);
            try (var result = pre.executeQuery()) {
                if (result.next()) {
                    user.setId(result.getLong("id"));
                    user.setUsername(result.getString("username"));
                    user.setEmail(result.getString("email"));
                    Set<Role> roles = new HashSet<>();
                    try (var preRole = conn.prepareStatement(sqlRole)) {
                        preRole.setLong(1, user.getId());
                        try (var rsRole = preRole.executeQuery();) {
                            while (result.next()) {
                                Role role = new Role();
                                role.setId(rsRole.getInt("id"));
                                role.setName(URole.valueOf(rsRole.getString("name")));
                                roles.add(role);
                            }
                        }
                    }
                    user.setRoles(roles);
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        String sqlRole = """
                SELECT r.id, r.name
                FROM roles r
                JOIN user_role ur ON r.id = ur.role_id
                WHERE ur.user_id = ?
                """;
        User user = new User();
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, email);
            try (var result = pre.executeQuery()) {
                if (result.next()) {
                    user.setId(result.getLong("id"));
                    user.setUsername(result.getString("username"));
                    user.setEmail(result.getString("email"));
                    user.setPassword(result.getString("password"));
                    Set<Role> roles = new HashSet<>();
                    try (var preRole = conn.prepareStatement(sqlRole)) {
                        preRole.setLong(1, user.getId());
                        try (var rsRole = preRole.executeQuery();) {
                            while (rsRole.next()) {
                                Role role = new Role();
                                role.setId(rsRole.getInt("id"));
                                role.setName(URole.valueOf(rsRole.getString("name")));
                                roles.add(role);
                            }
                        }
                    }
                    user.setRoles(roles);
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        String sqlRole = """
                SELECT r.id, r.name
                FROM roles r
                JOIN user_role ur ON r.id = ur.role_id
                WHERE ur.user_id = ?
                """;
        User user = new User();
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            pre.setString(1, username);
            try (var result = pre.executeQuery()) {
                if (result.next()) {
                    user.setId(result.getLong("id"));
                    user.setUsername(result.getString("username"));
                    user.setEmail(result.getString("email"));
                    user.setPassword(result.getString("password"));
                    Set<Role> roles = new HashSet<>();
                    try (var preRole = conn.prepareStatement(sqlRole)) {
                        preRole.setLong(1, user.getId());
                        try (var rsRole = preRole.executeQuery();) {
                            while (rsRole.next()) {
                                Role role = new Role();
                                role.setId(rsRole.getInt("id"));
                                role.setName(URole.valueOf(rsRole.getString("name")));
                                roles.add(role);
                            }
                        }
                    }
                    user.setRoles(roles);
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }


    @Override
    public User update(User u) {
        String sql = "UPDATE user SET username = ?, email = ?, password = ? WHERE id = ?";
        String deleteRoles = "DELETE FROM user_role WHERE user_id = ?";
        String insertRole = "INSERT INTO user_role(user_id, role_id) VALUES (?, ?)";
        String selectUser = "SELECT * FROM user WHERE id = ?";
        String selectRoles = """
                SELECT r.id, r.name
                FROM roles r
                JOIN user_role ur ON r.id = ur.role_id
                WHERE ur.user_id = ?
                """;
        User user = new User();
        try (var conn = datasource.getConnection(); var pre = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
//            1. Update user info
            pre.setString(1, u.getUsername());
            pre.setString(2, u.getEmail());
            pre.setString(3, u.getPassword());
            pre.setLong(4, u.getId());
            pre.executeUpdate();

            try (var del = conn.prepareStatement(deleteRoles)) {
                del.setLong(1, u.getId());
                del.executeUpdate();
            }

            try (var ins = conn.prepareStatement(insertRole)) {
                for (Role role : u.getRoles()) {
                    ins.setLong(1, u.getId());
                    ins.setInt(2, role.getId());
                    ins.addBatch();
                }
                ins.executeBatch();
            }


            try (var select = conn.prepareStatement(selectUser)) {
                select.setLong(1, u.getId());
                try (var result = pre.executeQuery()) {
                    if (result.next()) {
                        user.setId(result.getLong("id"));
                        user.setUsername(result.getString("username"));
                        user.setEmail(result.getString("email"));
                        user.setPassword(result.getString("password"));

                    } else {
                        throw new UserNotFoundException("Not match found with id " + u.getId());
                    }
                }
            }


            Set<Role> roles = new HashSet<>();
            try (var preRole = conn.prepareStatement(selectRoles)) {
                preRole.setLong(1, user.getId());
                try (var rsRole = preRole.executeQuery();) {
                    while (rsRole.next()) {
                        Role role = new Role();
                        role.setId(rsRole.getInt("id"));
                        role.setName(URole.valueOf(rsRole.getString("name")));
                        roles.add(role);
                    }
                }
            }
            user.setRoles(roles);
            conn.commit();
            return user;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (var conn = datasource.getConnection()) {
            var pre = conn.prepareStatement(sql);

            pre.setLong(1, id);

            int rows = pre.executeUpdate();

            if (rows == 0) {
                throw new UserNotFoundException("Not match found with id " + id);
            }
            return;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

}
