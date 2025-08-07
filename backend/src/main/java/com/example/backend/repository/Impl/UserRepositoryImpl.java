package com.example.backend.repository.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.example.backend.exception.wrapper.UserNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final DataSource datasource;

    public UserRepositoryImpl(DataSource datasource) {
	this.datasource = datasource;
    }

    public User save(User user) {
	String sql = "INSERT INTO user (name, surname, email, password) VALUES ( ?, ? ,? ,?)";
	try (Connection conn = datasource.getConnection()) {
	    PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

	    pre.setString(1, user.getName());
	    pre.setString(2, user.getSurname());
	    pre.setString(3, user.getEmail());
	    pre.setString(4, user.getPassword());
	    pre.executeUpdate();

	    try (ResultSet result = pre.getGeneratedKeys()) {
		if (result.next()) {
		    user.setId(result.getLong(1));
		}
	    }

	    return user;
	} catch (SQLException ex) {
	    ex.printStackTrace();
	    throw new RuntimeException(ex);
	}
    }

    @Override
    public List<User> findAll() {
	String sql = "SELECT * FROM user";
	List<User> users = new ArrayList<>();
	try (var conn = datasource.getConnection()) {
	    var pre = conn.prepareStatement(sql);

	    try (var result = pre.executeQuery()) {
		while (result.next()) {
		    var user = new User();

		    user.setId(result.getLong("id"));
		    user.setName(result.getString("name"));
		    user.setEmail(result.getString("email"));
		    user.setSurname(result.getString("surname"));

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
	User user = new User();
	try (var conn = datasource.getConnection()) {
	    var pre = conn.prepareStatement(sql);
	    pre.setLong(1, id);
	    try (var result = pre.executeQuery()) {
		if (result.next()) {
		    user.setId(result.getLong("id"));
		    user.setName(result.getString("name"));
		    user.setSurname(result.getString("surname"));
		    user.setEmail(result.getString("email"));
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
	User user = new User();
	try (var conn = datasource.getConnection()) {
	    var pre = conn.prepareStatement(sql);
	    pre.setString(1, email);
	    try (var result = pre.executeQuery()) {
		if (result.next()) {
		    user.setId(result.getLong("id"));
		    user.setName(result.getString("name"));
		    user.setSurname(result.getString("surname"));
		    user.setEmail(result.getString("email"));
		    user.setPassword(result.getString("password"));
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
	String sql = "UPDATE user SET name = ?, surname = ? , email = ? WHERE id = ?";
	String update = "SELECT * FROM user WHERE id = ?";
	User user = new User();
	try (var conn = datasource.getConnection()) {
	    var pre = conn.prepareStatement(sql);

	    pre.setString(1, u.getName());
	    pre.setString(2, u.getSurname());
	    pre.setString(3, u.getEmail());
	    pre.setLong(4, u.getId());
	    pre.executeUpdate();

	    try (var select = conn.prepareStatement(update)) {
		select.setLong(1, u.getId());
		try (var result = pre.executeQuery()) {
		    if (result.next()) {
			user.setId(result.getLong("id"));
			user.setName(result.getString("name"));
			user.setSurname(result.getString("surname"));
			user.setEmail(result.getString("email"));
			return user;
		    } else {
			throw new UserNotFoundException("Not match found with id " + u.getId());
		    }
		}
	    }
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
