package jack.mongo.response;

import lombok.Data;

import java.util.List;

@Data
public class ForexResponse {
    private Error error;
    private List<CurrencyData> currency;

    public ForexResponse(String code, String message) {
        this.error = new Error(code, message);
    }

    @Data
    public static class Error {
        private String code;
        private String message;

        public Error(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    @Data
    public static class CurrencyData {
        private String date;
        private String usd;

        public CurrencyData(String date, String usd) {
            this.date = date;
            this.usd = usd;
        }
    }
}