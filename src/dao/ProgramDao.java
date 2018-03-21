package dao;

import entity.Program;
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
public final class ProgramDao {

    private static final ProgramDao INSTANCE = new ProgramDao();
    private static final String SAVE = "INSERT INTO project_max.max_schema.program (name, price, description) VALUES (?, ?, ?)";
    private static final String GET_ALL = "SELECT id, name, description, price FROM project_max.max_schema.program";
    private static final String GET_BY_ID = GET_ALL + " WHERE id = ?";
    private static final String GET_BUYERS_BY_PROGRAM_ID = "SELECT user_id FROM max_schema.purchase WHERE program_id = ?";

    public void save(Program program) {
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, program.getName());
            preparedStatement.setDouble(2, program.getPrice());
            preparedStatement.setString(3, program.getDescription());
            preparedStatement.executeUpdate();

            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                program.setId(generatedKey.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Program getById(Integer programId) {
        Program program = null;
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {
            preparedStatement.setInt(1, programId);

            ResultSet resultSet = preparedStatement.executeQuery();
           program = new Program();
            if (resultSet.next()) {
                program.setId(programId);
                program.setName(resultSet.getString("name"));
                program.setDescription(resultSet.getString("description"));
                program.setPrice(resultSet.getDouble("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return program;
    }

    public List<Program> getAll() {
        List<Program> programs = new ArrayList<>();
        try (Connection connection = ConnectionUtil.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                programs.add(Program.builder()
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .price(resultSet.getDouble("price"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return programs;
    }

    public static ProgramDao getInstance() {
        return INSTANCE;
    }
}
