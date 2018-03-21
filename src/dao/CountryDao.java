package dao;

import entity.Country;
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
public final class CountryDao {

    private static final CountryDao INSTANCE = new CountryDao();
    private static final String GET_ALL = "SELECT id, name FROM project_max.max_schema.country";
    private static final String GET_BY_ID = "SELECT id, name FROM project_max.max_schema.country WHERE id = ?";
    private static final String SAVE = "INSERT INTO project_max.max_schema.country (name) VALUES (?)";

    public List<Country> getAll() {
        List<Country> countries = new ArrayList<>();
        try (Connection connection = ConnectionUtil.connect();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL);

            while (resultSet.next()) {
                countries.add(Country.builder()
                        .id(resultSet.getLong(1))
                        .name(resultSet.getString(2))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countries;
    }

    public Country getById(Long countryId) {
        Country country = null;
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {
            preparedStatement.setLong(1, countryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            country = new Country();
            while (resultSet.next()) {
                country.setId(resultSet.getLong(1));
                country.setName(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return country;
    }

    public void save(Country country) {
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, country.getName());
            preparedStatement.executeUpdate();

            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                country.setId(generatedKey.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CountryDao getInstance() {
        return INSTANCE;
    }
}
