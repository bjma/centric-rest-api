package api.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue()
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="brand")
    private String brand;

    // https://www.callicoder.com/hibernate-spring-boot-jpa-element-collection-demo/
    @ElementCollection
    @CollectionTable(name="tags", joinColumns=@JoinColumn(name="id"))
    private List<String> tags;

    @Column(name="category")
    private String category;

    @Column(name="created_at")
    private String createdAt;

    public Product() {}

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

    public UUID getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getBrand() { return brand; }

    public List<String> getTags() { return tags; }

    public String getCategory() { return category; }

    public String getCreatedAt() { return createdAt; }
}