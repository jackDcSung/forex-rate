package jack.mongo.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

/**
 * 使用者實體類
 */
@Data
@Builder
@Document("user") //指定表名
public class User {
    @Id
    private String id;

    private String name; // 姓名
    private int age; // 年齡
    private String email; // 電子郵件
    private String password; // 密碼
    private Date createTime; // 建立時間
}