package evolution.db2.config;

import evolution.db2.repository.ItemRepository;
import evolution.db2.repository.jpa.JpaItemRepositoryV3;
import evolution.db2.repository.practical.ItemQueryRepositoryV2;
import evolution.db2.repository.practical.ItemRepositoryV2;
import evolution.db2.service.ItemService;
import evolution.db2.service.ItemServiceV2;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PracticalConfig {

    private final ItemRepositoryV2 itemRepositoryV2;
    private final EntityManager em;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV2(itemRepositoryV2, queryRepository());
    }

    @Bean
    public ItemQueryRepositoryV2 queryRepository() {
        return new ItemQueryRepositoryV2(em);
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV3(em);
    }

}
