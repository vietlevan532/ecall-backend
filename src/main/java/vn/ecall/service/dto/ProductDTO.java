package vn.ecall.service.dto;

import lombok.*;
import vn.ecall.domain.businessmodel.Product;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Integer id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 50)
    private String price;

    @NotNull
    private String categoryName;

    @NotNull
    private Integer categoryId;

    @NotNull
    @Size(max = 50)
    private String shortDes;

    @NotNull
    private String fullDes;

    @Lob
    private String imageLink;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.categoryName = product.getCategory().getName();
        this.categoryId = product.getCategory().getCategory_id();
        this.shortDes = product.getShortDes();
        this.fullDes = product.getFullDes();
        this.imageLink = product.getImageLink();
    }

    /*
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getCategoryId(){return categoryId;}

    public void setCategoryId(Integer categoryId) {this.categoryId = categoryId;}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getShortDes() {
        return shortDes;
    }

    public void setShortDes(String shortDes) {
        this.shortDes = shortDes;
    }

    public String getFullDes() {
        return fullDes;
    }

    public void setFullDes(String fullDes) {
        this.fullDes = fullDes;
    }
*/


    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", shortDes='" + shortDes + '\'' +
                ", fullDes='" + fullDes + '\'' +
                ", imageLink='" + imageLink + '\'' +
                '}';
    }
}
