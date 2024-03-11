package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //이 프로젝트에서는 싱글 테이블 전략을 사용
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {


    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    //비즈니스 로직

    //재고 수량 증가 로직
    public void addStock (int quantity) {
        this.stockQuantity += quantity;
    }

    //재고 수량 감소 로직
    public void removeStock (int quantity) {
        int result = this.stockQuantity - quantity;
        if(result < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = result;
    }
}
