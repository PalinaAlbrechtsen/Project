package dao;

import entity.City;
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
public final class CityDao {

    private static final CityDao INSTANCE = new CityDao();
    private static final String SAVE =
            "INSERT INTO project_max.max_schema.city (name) VALUES (?) ";
    private static final String GET_ALL =
            "SELECT id, name FROM project_max.max_schema.city";
    private static final String GET_CITY_BY_ID =
            "SELECT id, name FROM project_max.max_schema.city WHERE id = ?";

    public List<City> getAll() {
        List<City> cities = new ArrayList<>();
        try (Connection connection = ConnectionUtil.connect();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL);
            while (resultSet.next()) {
                cities.add(City.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cities;
    }

    public City getById(Long cityId) {
        City city = null;
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CITY_BY_ID)) {
            preparedStatement.setLong(1, cityId);

            ResultSet resultSet = preparedStatement.executeQuery();
            city = new City();
            if (resultSet.next()) {
                city.setId(resultSet.getLong(1));
                city.setName(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return city;
    }

    public void save(City city) {
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement cityStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            cityStatement.setString(1, city.getName());
            cityStatement.executeUpdate();

            ResultSet resultSet = cityStatement.getGeneratedKeys();
            if (resultSet.next()) {
                city.setId(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CityDao getInstance() {
        return INSTANCE;
    }
}
