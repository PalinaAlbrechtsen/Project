package dao;

import entity.Role;
import entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserDao {

    private static final UserDao INSTANCE = new UserDao();
    private static final String SAVE = "INSERT INTO project_max.max_schema.user (email, password, role_id) VALUES (?, ?, ?)";
    private static final String GET_BY_ID = "SELECT u.email, u.password, r.id ,r.name " +
            "FROM max_schema.user AS u INNER JOIN max_schema.role AS r ON u.role_id = r.id";

    public void save(User user) {
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getRole().getId());
            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                user.setId(generatedKey.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getById(Long id) {
        User user = null;
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {
            user = new User();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = User.builder()
                        .id(id)
                        .email(resultSet.getString("email"))
                        .password(resultSet.getString("password"))
                        .role(Role.builder()
                                .id(resultSet.getInt("id"))
                                .name(resultSet.getString("name"))
                                .build())
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
