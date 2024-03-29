package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    //아이템 저장 기능
    @Transactional //readOnly=true값을 안주게되면 기본 디폴트값은 false라서 맨위에서 readOnly true를 해줬기때문에 조회가 아닌 기능들은 다 false로 해줘야함.
    public Long saveItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    //아이템 전체 조회 기능
    public List<Item> findItem() {
        return itemRepository.findAll();
    }

    //아이템 단일 조회 기능
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }



}
