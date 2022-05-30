import com.zerobase.swififinder.service.WifiInfoService;
import org.junit.jupiter.api.Test;

public class WifiServiceTest {

    @Test
    void orderedSelectTest() {
        double myLat = 37.5544069;
        double myLnt = 126.8998666;
        WifiInfoService wifiInfoService = new WifiInfoService();
        wifiInfoService.wifiOrderedSelect(String.valueOf(myLat), String.valueOf(myLnt), 20);
    }

}