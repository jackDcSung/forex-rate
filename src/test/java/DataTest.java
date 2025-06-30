import jack.mongo.ForexRateApplication;
import jack.mongo.service.ForexService;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ForexRateApplication.class)
public class DataTest {

    @Autowired
    ForexService forexService;

    @Test
    public void test1() {
        String s = HttpUtil.get("https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates");
        System.out.println(s);
    }

    @Test
    public void test2() {
        forexService.fetchAndSaveUsdNtdRate();
    }
}
