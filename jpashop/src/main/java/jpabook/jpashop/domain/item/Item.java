package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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


}
