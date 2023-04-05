import org.bridgelabz.database.SQLOperations;
import org.junit.Assert;
import org.junit.Test;

public class SQLOperationsTest {

    @Test(expected = Exception.class)
    public void callToCreateDatabaseWithoutConnectingServer_ShouldThrowException() {
        SQLOperations.getInstance().createDatabase();
    }

    @Test
    public void callToCreateDatabaseAfterConnectingServer_ShouldReturnGreaterThanZero() {
        SQLOperations sql=SQLOperations.getInstance();
        sql.connectToServer();
        int rowsAffected=sql.createDatabase();
        Assert.assertTrue(rowsAffected>0);
    }
}
