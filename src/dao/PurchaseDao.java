package dao;

import entity.Program;
import entity.Purchase;
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
public final class PurchaseDao {

    private static final PurchaseDao INSTANCE = new PurchaseDao();
    private static final String GET_ALL = "SELECT p.id, p.date, p.program_id, s.name, prog.name, prog.price " +
            "FROM max_schema.purchase AS p " +
            "INNER JOIN max_schema.subscriber AS s ON p.user_id = s.user_id " +
            "INNER JOIN max_schema.program AS prog ON p.program_id = prog.id";
    private static final String GET_BY_USER_ID = GET_ALL + " WHERE p.user_id = ?";
    private static final String GET_BY_PROGRAM_ID = GET_ALL + " WHERE program_id = ?";
    private static final String SAVE = "INSERT INTO max_schema.purchase (user_id, program_id, date) VALUES (?, ?, ?)";

    public List<Purchase> getByUserId(Long subscriberId) {
        List<Purchase> purchases = new ArrayList<>();
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_USER_ID)) {
            preparedStatement.setLong(1, subscriberId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                purchases.add(Purchase.builder()
                        .id(resultSet.getLong("id"))
                        .subscriber(SubscriberDao.getInstance().getById(subscriberId))
                        .program(ProgramDao.getInstance().getById(resultSet.getInt("program_id")))
                        .dateOfPurchase(resultSet.getString("date"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return purchases;
    }

    public List<Subscriber> getByProgramId(Integer programId) {
        List<Subscriber> subscribers = new ArrayList<>();
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_PROGRAM_ID)) {
            preparedStatement.setInt(1, programId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                subscribers.add(SubscriberDao.getInstance().getById(resultSet.getLong("user_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subscribers;
    }

    public void save(Subscriber subscriber, Program program) {
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate date = LocalDate.now();
            preparedStatement.setLong(1, subscriber.getUser().getId());
            preparedStatement.setInt(2, program.getId());
            preparedStatement.setString(3, datePattern.format(date));
            preparedStatement.executeUpdate();
            Purchase purchase = new Purchase();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                purchase.setId(generatedKey.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PurchaseDao getInstance() {
        return INSTANCE;
    }
}
