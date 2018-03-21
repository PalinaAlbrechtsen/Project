package util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConnectionUtil {

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(PropertiesUtil.get("url"),
                PropertiesUtil.get("user"),
                PropertiesUtil.get("password"));
    }
}
