package jack.mongo.repo;

import jack.mongo.domain.ForeignExchangeRate;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ForexRepository extends MongoRepository<ForeignExchangeRate, String> {
    List<ForeignExchangeRate> findByDateBetween(LocalDateTime start, LocalDateTime end);
}