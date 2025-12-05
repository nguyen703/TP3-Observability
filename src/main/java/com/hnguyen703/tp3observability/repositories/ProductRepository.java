package com.hnguyen703.tp3observability.repositories;

import com.hnguyen703.tp3observability.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
