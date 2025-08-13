package evolution.db2.service;

import evolution.db2.domain.Item;
import evolution.db2.repository.ItemSearchCond;
import evolution.db2.repository.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCond itemSearch);
}
