package jack.mongo.service;

import jack.mongo.param.ForexRequest;
import jack.mongo.response.ForexResponse;

public interface ForexService {

    void fetchAndSaveUsdNtdRate();

    ForexResponse getHistoricalRates(ForexRequest request);
}
