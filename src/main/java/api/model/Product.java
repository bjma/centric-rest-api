package api.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private UUID id;

    private String name;

    private String description;

    private String brand;

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

    public UUID getId() {
        return id;
    } 

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getCategory() {
        return category;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}