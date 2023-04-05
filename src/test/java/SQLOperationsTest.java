import org.bridgelabz.database.SQLOperations;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class SQLOperationsTest {

    @Test(expected = Exception.class)
    public void callToCreateDatabaseWithoutConnectingServer_ShouldThrowException() throws SQLException {
        SQLOperations.getInstance().createDatabase();
    }

    @Test
    public void callToCreateDatabaseAfterConnectingServer_ShouldReturnGreaterThanZero() throws SQLException {
        SQLOperations sql=SQLOperations.getInstance();
        sql.connectToServer();
        int rowsAffected=sql.createDatabase();
        Assert.assertTrue(rowsAffected>0);
    }

}
