package dao;

import entity.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoleDao {

    private static final RoleDao INSTANCE = new RoleDao();
    private static final String GET = "SELECT id, name FROM project_max.max_schema.role WHERE id = ?";
    private static final String SAVE = "INSERT INTO max_schema.role (name) VALUES (?)";

    public Role getById(Integer id) {
        Role role = null;
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            role = new Role();
            while (resultSet.next()) {
                role.setId(id);
                role.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }

    public void save(Role role) {
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, role.getName());
            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                role.setId(generatedKey.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static RoleDao getInstance() {
        return INSTANCE;
    }
}
