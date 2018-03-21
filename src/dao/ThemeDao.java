package dao;

import entity.Theme;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThemeDao {

    private static final ThemeDao INSTANCE = new ThemeDao();
    private static final String GET_ALL = "SELECT id, name FROM max_schema.theme";
    private static final String SAVE = "INSERT INTO project_max.max_schema.theme (name) VALUES (?)";
    private static final String GET_BY_ID = "SELECT id, name FROM max_schema.theme WHERE id = ?";

    public List<Theme> getAll() {
        List<Theme> themes = new ArrayList<>();
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                themes.add(Theme.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return themes;
    }

    public void save(Theme theme) {
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, theme.getName());
            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                theme.setId(generatedKey.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Theme getById(int id) {
        Theme theme = null;
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement statement = connection.prepareStatement(GET_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            theme = new Theme();
            if (resultSet.next()) {
                theme.setId(id);
                theme.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theme;
    }

    public static ThemeDao getInstance() {
        return INSTANCE;
    }
}
