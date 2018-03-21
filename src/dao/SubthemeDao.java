package dao;

import entity.Subtheme;
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
public final class SubthemeDao {

    private static final SubthemeDao INSTANCE = new SubthemeDao();
    private static final String GET_BY_THEME_ID = "SELECT id, name, text, theme_id FROM max_schema.subtheme WHERE theme_id = ?";
    private static final String SAVE = "INSERT INTO max_schema.subtheme (name, text, theme_id) VALUES (?, ?, ?)";

    public List<Subtheme> getByThemeId(int id) {
        List<Subtheme> subThemesOfTheme = new ArrayList<>();
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_THEME_ID)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                subThemesOfTheme.add(Subtheme.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .text(resultSet.getString("text"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subThemesOfTheme;
    }

    public void save(Subtheme subtheme) {
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement subthemeStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            subthemeStatement.setString(1, subtheme.getName());
            subthemeStatement.setString(2, subtheme.getText());
            subthemeStatement.setInt(3, subtheme.getTheme().getId());
            subthemeStatement.executeUpdate();

            ResultSet resultSet = subthemeStatement.getGeneratedKeys();
            if (resultSet.next()) {
                subtheme.setId(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SubthemeDao getInstance() {
        return INSTANCE;
    }
}
