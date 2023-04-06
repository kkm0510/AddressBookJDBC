import org.bridgelabz.database.DatabaseOperations;
import org.bridgelabz.database.SQLOperations;
import org.junit.Assert;
import org.junit.Test;

public class DatabaseOperationsTest {

    @Test(expected = Exception.class)
    public void callToCreateDatabaseWithoutConnectingServer_ShouldThrowException() {
        DatabaseOperations db = SQLOperations.getInstance();
        db.createDatabase();
    }

    @Test
    public void callToCreateDatabaseAfterConnectingServer_ShouldReturnGreaterThanZero() {
        DatabaseOperations db = SQLOperations.getInstance();
        db.connectToServer();
        int rowsAffected = db.createDatabase();
        Assert.assertTrue(rowsAffected > 0);
    }
}
