package api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue()
    private UUID id;

    private String name;

    private String description;

    private String brand;

    // https://www.callicoder.com/hibernate-spring-boot-jpa-element-collection-demo/
    @ElementCollection
    @CollectionTable(name="tags", joinColumns=@JoinColumn(name="id"))
    private List<String> tags;

    private String category;

    @Column(name="created_at")
    private String createdAt;

    public Product(UUID id,
                   String name,
                   String description,
                   String brand,
                   List<String> tags,
                   String category,
                   String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.tags = tags;
        this.category = category;
        this.createdAt = createdAt;
    }

    public void setId(UUID id) { this.id = id; }

    public void setCreatedAt(String created_at) { this.createdAt = created_at; }
}