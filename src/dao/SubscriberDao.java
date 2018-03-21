package dao;

import entity.Gender;
import entity.Subscriber;
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
public final class SubscriberDao {

    private static final SubscriberDao INSTANCE = new SubscriberDao();
    private static final String SAVE = "INSERT INTO project_max.max_schema.subscriber (name, birth_date, user_id, gender, city_id, country_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_BY_ID = "SELECT user_id, name, birth_date, gender, city_id, country_id FROM max_schema.subscriber WHERE user_id = ?";
    private static final String GET_BY_BIRTHDAY = "SELECT user_id FROM max_schema.subscriber WHERE birth_date = ?";
    private static final String GET_ALL = "SELECT user_id, name, birth_date, gender, city_id, country_id FROM max_schema.subscriber";
    private static final String GET_BY_COUNTRY_ID = GET_ALL + " WHERE country_id = ?";
    private static final String GET_BY_CITY_ID = GET_ALL + " WHERE city_id = ?";

    public void save(Subscriber subscriber) {
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, subscriber.getName());
            preparedStatement.setString(2, subscriber.getDateOfBirth());
            preparedStatement.setLong(3, subscriber.getUser().getId());
            preparedStatement.setString(4, subscriber.getGender().toString());
            preparedStatement.setLong(5, subscriber.getCity().getId());
            preparedStatement.setLong(6, subscriber.getCountry().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Subscriber getById(Long userId) {
        Subscriber subscriber = null;
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {
            preparedStatement.setLong(1, userId);
            subscriber = new Subscriber();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                subscriber = buildSubscriberFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subscriber;
    }

    public List<Subscriber> getByBirthDate(String date) {
        List<Subscriber> subscribers = new ArrayList<>();
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_BIRTHDAY)) {
            preparedStatement.setString(1, date);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                subscribers.add(getById(resultSet.getLong("user_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subscribers;
    }

    public List<Subscriber> getByCountryId(Long countryId) {
        return createListOfSubscribersFromSomewhere(GET_BY_COUNTRY_ID, countryId);
    }

    public List<Subscriber> getByCityId(Long cityId) {
        return createListOfSubscribersFromSomewhere(GET_BY_CITY_ID, cityId);
    }

    private List<Subscriber> createListOfSubscribersFromSomewhere(String sql, Long value) {
        List<Subscriber> subscribers = new ArrayList<>();
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, value);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                subscribers.add(buildSubscriberFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subscribers;
    }

    private Subscriber buildSubscriberFromResultSet(ResultSet resultSet) throws SQLException {
        return Subscriber.builder()
                .user(UserDao.getInstance().getById(resultSet.getLong("user_id")))
                .name(resultSet.getString("name"))
                .dateOfBirth(resultSet.getString("birth_date"))
                .gender(Gender.valueOf(resultSet.getString("gender")))
                .city(CityDao.getInstance().getById(resultSet.getLong("city_id")))
                .country(CountryDao.getInstance().getById(resultSet.getLong("country_id")))
                .build();
    }

    public static SubscriberDao getInstance() {
        return INSTANCE;
    }
}
