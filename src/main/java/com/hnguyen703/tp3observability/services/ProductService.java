package com.hnguyen703.tp3observability.services;
@org.springframework.stereotype.Service
public class ProductService {
    private final com.hnguyen703.tp3observability.repositories.ProductRepository productRepository;

    public ProductService(com.hnguyen703.tp3observability.repositories.ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public java.util.List<com.hnguyen703.tp3observability.models.Product> getAllProducts(java.lang.String userId) {
        logger.info("{\"timestamp\": \"" + java.time.Instant.now() + "\", \"user\": \"" + userId + "\", \"type\": \"READ\", \"method\": \"getAllProducts\"}");;
        return productRepository.findAll();
    }

    public com.hnguyen703.tp3observability.models.Product getProductById(java.lang.String id, java.lang.String userId) {
        logger.info("{\"timestamp\": \"" + java.time.Instant.now() + "\", \"user\": \"" + userId + "\", \"type\": \"READ\", \"method\": \"getProductById\"}");;
        return productRepository.findById(id).orElseThrow(() -> new java.lang.RuntimeException("Product not found with ID: " + id));
    }

    public com.hnguyen703.tp3observability.models.Product addProduct(com.hnguyen703.tp3observability.models.Product product, java.lang.String userId) {
        logger.info("{\"timestamp\": \"" + java.time.Instant.now() + "\", \"user\": \"" + userId + "\", \"type\": \"WRITE\", \"method\": \"addProduct\"}");;
        if (productRepository.existsById(product.getId())) {
            throw new java.lang.RuntimeException("Product already exists with ID: " + product.getId());
        }
        return productRepository.save(product);
    }

    public void deleteProduct(java.lang.String id, java.lang.String userId) {
        logger.info("{\"timestamp\": \"" + java.time.Instant.now() + "\", \"user\": \"" + userId + "\", \"type\": \"WRITE\", \"method\": \"deleteProduct\"}");;
        if (!productRepository.existsById(id)) {
            throw new java.lang.RuntimeException("Cannot delete. Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    public com.hnguyen703.tp3observability.models.Product updateProduct(java.lang.String id, com.hnguyen703.tp3observability.models.Product updatedProduct, java.lang.String userId) {
        logger.info("{\"timestamp\": \"" + java.time.Instant.now() + "\", \"user\": \"" + userId + "\", \"type\": \"WRITE\", \"method\": \"updateProduct\"}");;
        if (!productRepository.existsById(id)) {
            throw new java.lang.RuntimeException("Cannot update. Product not found with ID: " + id);
        }
        updatedProduct.setId(id);
        return productRepository.save(updatedProduct);
    }

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProductService.class);
}
