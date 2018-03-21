package dao;

import entity.Param;
import entity.Subscriber;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParamDao {

    private static final ParamDao INSTANCE = new ParamDao();
    private static final String SAVE = "INSERT INTO max_schema.params (subscriber_id, date, height, weight) VALUES (?, ?, ?, ?)";
    private static final String GET = "SELECT id, subscriber_id, date, height, weight FROM project_max.max_schema.params WHERE subscriber_id = ?";
    private static final String GET_PARAMS_BY_SUBSCRIBER_ID = "SELECT id, subscriber_id, date, height, weight FROM max_schema.params WHERE subscriber_id = ?";

    public void save(Subscriber subscriber, Param param) {
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate date = LocalDate.now();
            preparedStatement.setLong(1, subscriber.getUser().getId());
            preparedStatement.setString(2, datePattern.format(date));
            preparedStatement.setLong(3, param.getHeight());
            preparedStatement.setDouble(4, param.getWeight());
            preparedStatement.executeUpdate();

            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                param.setId(generatedKey.getLong("id"));
            }
//            subscriber.getParams().add(param);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Param> getHistoryBySubscriberId(Long subscriberId) {
        List<Param> params = new ArrayList<>();
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PARAMS_BY_SUBSCRIBER_ID)) {
            preparedStatement.setLong(1, subscriberId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                params.add(Param.builder()
                        .id(resultSet.getLong("id"))
                        .subscriber(SubscriberDao.getInstance().getById(subscriberId))
                        .date(resultSet.getString("date"))
                        .height(resultSet.getInt("height"))
                        .weight(resultSet.getDouble("weight"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static ParamDao getInstance() {
        return INSTANCE;
    }
}
