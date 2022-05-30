import com.zerobase.swififinder.service.LocationHistoryService;
import org.junit.jupiter.api.Test;

public class HistoryServiceTest {
    @Test
    void selectTest() {
        LocationHistoryService locationHistoryService = new LocationHistoryService();
        locationHistoryService.historySelect();
    }

    @Test
    void insertTest() {
        LocationHistoryService locationHistoryService = new LocationHistoryService();
        String lat = "12.123";
        String lnt = "34.345";

        locationHistoryService.historyInsert(lat, lnt);
    }

    @Test
    void deleteTest() {
        LocationHistoryService locationHistoryService = new LocationHistoryService();
        locationHistoryService.historyDelete("3");
    }
}
