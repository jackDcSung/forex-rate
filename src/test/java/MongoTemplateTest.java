import jack.mongo.ForexRateApplication;
import jack.mongo.domain.User;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.List;

/**
 * 使用MongoTemplate操作mongo
 */
@SpringBootTest(classes = ForexRateApplication.class)
public class MongoTemplateTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    // 新增
    @Test
    public void testInsert() {
        User user = User.builder()
                .name("test")
                .age(20)
                .createTime(new Date())
                .build();
        User obj = mongoTemplate.insert(user);
        assert obj != null;
        System.out.println("新增成功");
    }

    // 查詢全部
    @Test
    public void testFindAll() {
        List<User> userList = mongoTemplate.findAll(User.class);
        userList.forEach(System.out::println);
    }

    // 根據id查
    @Test
    public void testFindById() {
        User user = mongoTemplate.findById("669a36442f07de177e4f92f5", User.class);
        System.out.println("user = " + user);
    }

    // 條件查詢
    @Test
    public void testCondition() {
        // where name = 'test' and age = 20
        List<User> userList = mongoTemplate.find(
                // 查詢條件，透過Criteria物件構建
                Query.query(Criteria.where("name").is("test").and("age").is(20)),
                // 查詢結果型態
                User.class
        );
        System.out.println("userList = " + userList);
    }

    // 分頁查詢
    @Test
    public void testPage() {
        // 取得第1頁，每頁2筆資料
        List<User> userList =
                mongoTemplate.find(
                        // 查詢條件
                        new Query()
                                .skip(0)  // 跳過前0筆資料
                                .limit(2), // 取2筆資料
                        // 查詢結果型態
                        User.class
                );
        System.out.println("userList = " + userList);
    }

    // 刪除
    @Test
    public void testDelete() {
        DeleteResult result = mongoTemplate.remove(
                Query.query(Criteria.where("_id").is("669a36442f07de177e4f92f5")),
                User.class
        );
        long count = result.getDeletedCount();
        System.out.println("count = " + count);
    }

    // 修改
    @Test
    public void testUpdate() {
        UpdateResult result = mongoTemplate.upsert(
                // 查詢條件
                Query.query(Criteria.where("name").is("test")),
                // 修改內容
                Update.update("age", 21),
                // 修改結果型態
                User.class
        );
        // 影響筆數
        long count = result.getMatchedCount();
        System.out.println("count = " + count);
    }
}
