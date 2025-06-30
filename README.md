# forex-rate



這是一個用 Spring Boot 開發的匯率查詢服務，會自動從台灣期貨交易所（TAIFEX）抓取每日美元兌新台幣（USD/NTD）匯率，並把資料存進 MongoDB，提供簡單的 API 讓你查詢歷史匯率。

---

## 技術使用

- Java 8
- Spring Boot 2.7.10
- MongoDB
- Hutool 工具包
- Lombok

---

## 安裝與啟動方式

1. **安裝並啟動 MongoDB**

   - 預設連線：`127.0.0.1:27017`，資料庫名稱：`testdb1`
   - 你可以在 `src/main/resources/application.yml` 調整連線設定

2. **下載專案並安裝相依套件**

   ```bash
   git clone <專案網址>
   cd forex-rate/forex-rate
   mvn clean install
   ```

3. **啟動專案**
   ```bash
   mvn spring-boot:run
   ```
   預設會跑在 `http://localhost:8089`

---

## API 使用說明

### 1. 查詢歷史匯率

- **路徑**：`POST /history`
- **請求格式（JSON）**：

  ```json
  {
    "startDate": "2023/06/01",
    "endDate": "2023/06/30",
    "currency": "usd"
  }
  ```

  - `startDate`、`endDate`：查詢的日期區間，格式是 `yyyy/MM/dd`，只能查一年內、而且不能超過昨天。
  - `currency`：目前只支援 `usd`。

- **回應格式（JSON）**：
  ```json
  {
    "error": { "code": "0000", "message": "成功" },
    "currency": [
      { "date": "20230601", "usd": "30.8" },
      { "date": "20230602", "usd": "30.7" }
      // ...
    ]
  }
  ```
  - 如果有錯誤，`error.code` 會顯示錯誤代碼，`message` 會說明原因。

---

## 資料庫設定

`src/main/resources/application.yml` 範例：

```yaml
spring:
  data:
    mongodb:
      database: testdb1
      host: 127.0.0.1
      port: 27017
server:
  port: 8089
```

---

## 其他補充

- 匯率資料來源：[台灣期貨交易所開放資料](https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates)
- 專案內建自動排程（你可以在 `ForexDataScheduler` 把註解打開）每天自動抓匯率。
- 想擴充其他幣別或功能，可以參考 `ForexService` 跟 `ForexServiceImpl`。
