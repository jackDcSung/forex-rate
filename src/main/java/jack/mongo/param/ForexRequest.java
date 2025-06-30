package jack.mongo.param;

import lombok.Data;

@Data
public class ForexRequest {
    private String startDate; // 格式: yyyy/MM/dd
    private String endDate;   // 格式: yyyy/MM/dd
    private String currency; // 幣別 (usd)
}