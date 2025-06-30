package jack.mongo.job;

import jack.mongo.service.ForexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForexDataScheduler {

    private final ForexService forexService;

    @Autowired
    public ForexDataScheduler(ForexService forexService) {
        this.forexService = forexService;
    }

    // @Scheduled(cron = "0 0 18 * * ?") // 每天18:00執行
    // 每分鐘一次
    // @Scheduled(fixedRate = 60000)
    public void fetchAndSaveForexData() {
        forexService.fetchAndSaveUsdNtdRate();
    }
}
