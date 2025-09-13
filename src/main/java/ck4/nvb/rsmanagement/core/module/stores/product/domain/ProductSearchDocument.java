package ck4.nvb.rsmanagement.core.module.stores.product.domain;

import ck4.nvb.rsmanagement.base.search.base.BaseSearchDocument;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "products")
@Getter
@Setter
public class ProductSearchDocument extends BaseSearchDocument<Long> {
    @Field(type = FieldType.Text, analyzer = "standard")
    private String name;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String sku;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String desc;

    @Field(type = FieldType.Integer)
    private Integer unitPrice;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Override
    public void fromEntity(Object entity) {
        if (entity instanceof Product) {
            Product product = (Product) entity;
            setId(product.getId());
            setName(product.getName());
            setSku(product.getSku());
            setDesc(product.getDescription());
            setUnitPrice(product.getUnitPrice());
            setCategoryId(product.getCategoryId());
        }
    }

    @Override
    public String getSearchableContent() {
        return (name != null ? name + " " : "") +
                (desc != null ? desc : "");    }
}
