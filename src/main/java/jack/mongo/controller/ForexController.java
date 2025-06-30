package jack.mongo.controller;

import jack.mongo.param.ForexRequest;
import jack.mongo.response.ForexResponse;
import jack.mongo.service.ForexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForexController {

    private final ForexService forexService;

    @Autowired
    public ForexController(ForexService forexService) {
        this.forexService = forexService;
    }

    @PostMapping("/history")
    public ResponseEntity<ForexResponse> getForexHistory(@RequestBody ForexRequest request) {
        ForexResponse response = forexService.getHistoricalRates(request);
        return ResponseEntity.ok(response);
    }
}
