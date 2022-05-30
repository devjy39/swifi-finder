import com.zerobase.swififinder.util.ApiExplorer;
import org.junit.jupiter.api.Test;

public class ApiTest {

    @Test
    void apiTest() {
        ApiExplorer apiExplorer = new ApiExplorer();
        apiExplorer.indexDataMigration(1, 10);
    }

    @Test
    void loadApiData() {
        ApiExplorer apiExplorer = new ApiExplorer();
        apiExplorer.openApiDataMigration();
    }
}