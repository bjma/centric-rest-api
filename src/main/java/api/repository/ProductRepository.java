package api.repository;

import api.model.Product;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Page<Product> findAllByCategory(String category, Pageable pageable);
}