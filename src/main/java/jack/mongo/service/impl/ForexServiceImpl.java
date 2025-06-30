package jack.mongo.service.impl;

import jack.mongo.domain.ForeignExchangeRate;
import jack.mongo.param.ForexRequest;
import jack.mongo.repo.ForexRepository;
import jack.mongo.response.ForexResponse;
import jack.mongo.service.ForexService;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ForexServiceImpl implements ForexService {

    private static final String API_URL = "https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates";
    private final ForexRepository forexRepository;

    @Autowired
    public ForexServiceImpl(ForexRepository forexRepository) {
        this.forexRepository = forexRepository;
    }

    @Override
    public void fetchAndSaveUsdNtdRate() {
        String res = HttpUtil.get(API_URL);
        log.info("res:{}", res);
        // 解析json
        JSONArray result = JSONUtil.parseArray(res);
        for (Object o : result) {
            JSONObject jsonObject = JSONUtil.parseObj(o.toString());
            String date = jsonObject.getStr("Date");
            String usdNtd = jsonObject.getStr("USD/NTD");
            ForeignExchangeRate forexRate = new ForeignExchangeRate();
            forexRate.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd")));
            forexRate.setUsdToNtd(usdNtd);
            // 保存到mongo
            forexRepository.insert(forexRate);
        }
    }

    @Override
    public ForexResponse getHistoricalRates(ForexRequest request) {
        if (!"usd".equalsIgnoreCase(request.getCurrency())) {
            return new ForexResponse("E002", "不支援的幣別");
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        try {
            LocalDate startDate = LocalDate.parse(request.getStartDate(), inputFormatter);
            LocalDate endDate = LocalDate.parse(request.getEndDate(), inputFormatter);

            LocalDate today = LocalDate.now();
            LocalDate oneYearAgo = today.minusYears(1);
            LocalDate yesterday = today.minusDays(1);

            if (startDate.isBefore(oneYearAgo) ||
                    endDate.isAfter(yesterday) ||
                    startDate.isAfter(endDate)) {

                return new ForexResponse("E001", "日期區間不符");
            }

            // 查詢資料庫
            List<ForeignExchangeRate> rates = forexRepository
                    .findByDateBetween(
                            startDate.atStartOfDay(),
                            endDate.atTime(23, 59, 59)
                    );

            // 轉換回應格式
            List<ForexResponse.CurrencyData> currencyData = rates.stream()
                    .map(rate -> new ForexResponse.CurrencyData(
                            rate.getDate().format(outputFormatter),
                            rate.getUsdToNtd()
                    ))
                    .collect(Collectors.toList());

            ForexResponse response = new ForexResponse("0000", "成功");
            response.setCurrency(currencyData);
            return response;

        } catch (DateTimeParseException e) {
            return new ForexResponse("E003", "日期格式錯誤");
        }
    }
}
