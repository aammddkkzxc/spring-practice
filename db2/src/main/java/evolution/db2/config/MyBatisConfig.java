package evolution.db2.config;

import evolution.db2.repository.ItemRepository;
import evolution.db2.repository.mybatis.ItemMapper;
import evolution.db2.repository.mybatis.MyBatisItemRepository;
import evolution.db2.service.ItemService;
import evolution.db2.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final ItemMapper itemMapper;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new MyBatisItemRepository(itemMapper);
    }
}