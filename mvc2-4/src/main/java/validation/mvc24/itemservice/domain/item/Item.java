package validation.mvc24.itemservice.domain.item;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Item {

    private Long id;

//    @NotBlank
    private String itemName;

//    @NotNull
//    @Range(min = 1000, max = 1000000)
    private Integer price;

//    @NotNull
//    @Max(9999)
    private Integer quantity;

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
