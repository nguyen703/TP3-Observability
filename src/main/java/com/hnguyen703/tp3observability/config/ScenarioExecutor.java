package com.hnguyen703.tp3observability.config;

import com.hnguyen703.tp3observability.models.Product;
import com.hnguyen703.tp3observability.services.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class ScenarioExecutor implements CommandLineRunner {

    private final ProductService productService;
    private final Random random = new Random();

    public ScenarioExecutor(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- STARTING AUTOMATED SCENARIO EXECUTION ---");

        // We will create 10 users
        // Users 1-4: Readers (Mostly Read operations)
        // Users 5-7: Writers (Mostly Write operations)
        // Users 8-10: Mixed/Random

        // Pre-populate some data so Readers have something to read
        seedInitialData();

        // Execute scenarios
        simulateUserBehavior("User_Reader_1", 0.90); // 90% Read chance
        simulateUserBehavior("User_Reader_2", 0.85);
        simulateUserBehavior("User_Reader_3", 0.90);
        simulateUserBehavior("User_Reader_4", 0.80);

        simulateUserBehavior("User_Writer_1", 0.20); // 20% Read chance (mostly write)
        simulateUserBehavior("User_Writer_2", 0.30);
        simulateUserBehavior("User_Writer_3", 0.10);

        simulateUserBehavior("User_Random_1", 0.50); // 50/50 split
        simulateUserBehavior("User_Random_2", 0.50);
        simulateUserBehavior("User_Random_3", 0.50);

        System.out.println("--- SCENARIO EXECUTION FINISHED ---");
    }

    private void seedInitialData() {
        try {
            for (int i = 1; i <= 20; i++) {
                // We use a system user to seed data
                String id = "P" + i;
                // Only add if it doesn't exist
                try {
                    productService.getProductById(id, "System_Seeder");
                } catch (RuntimeException e) {
                    productService.addProduct(new Product(id, "Product " + i, 10.0 * i, LocalDate.now().plusDays(30)), "System_Seeder");
                }
            }
        } catch (Exception e) {
            // Ignore errors during seeding
        }
    }

    /**
     * Simulates ~20 operations for a single user.
     * @param userId The ID of the user
     * @param readProbability 0.0 to 1.0 (Higher = more reads)
     */
    private void simulateUserBehavior(String userId, double readProbability) {
        List<String> productIds = new ArrayList<>();
        // Assume products P1 to P20 exist
        for(int i=1; i<=20; i++) productIds.add("P"+i);

        for (int i = 0; i < 20; i++) {
            try {
                double roll = random.nextDouble();

                if (roll < readProbability) {
                    // --- PERFORM READ ---
                    if (random.nextBoolean()) {
                        productService.getAllProducts(userId);
                    } else {
                        String randomId = productIds.get(random.nextInt(productIds.size()));
                        productService.getProductById(randomId, userId);
                    }
                } else {
                    // --- PERFORM WRITE ---
                    int action = random.nextInt(3); // 0=Add, 1=Update, 2=Delete
                    String randomId = productIds.get(random.nextInt(productIds.size()));

                    switch (action) {
                        case 0: // Add
                            String newId = "NEW_" + userId + "_" + i;
                            Product p = new Product(newId, "New Item", 99.99, LocalDate.now());
                            productService.addProduct(p, userId);
                            break;
                        case 1: // Update
                            Product updateP = new Product(randomId, "Updated Name", 50.0, LocalDate.now());
                            productService.updateProduct(randomId, updateP, userId);
                            break;
                        case 2: // Delete
                            // We try to delete, but catch exception if it's already gone
                            try {
                                productService.deleteProduct(randomId, userId);
                            } catch (Exception e) { /* expected if already deleted */ }
                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                // Ignore business exceptions (e.g. "Product not found") during simulation
                // We just want the logs to be generated.
            }
        }
    }
}