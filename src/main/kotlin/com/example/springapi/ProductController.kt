package com.example.springapi

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/{userId}/products")
class ProductController(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) {

    @GetMapping
    fun getUserProducts(@PathVariable userId: Long): List<Product> {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("Invalid user Id:$userId") }
        return productRepository.findByUser(user)
    }

    @PostMapping
    fun addProduct(
        @RequestBody request: CreateProductRequest,
        @PathVariable userId: Long
    ): String {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("Invalid user Id:$userId") }
        val product = Product(name = request.name, cost = request.cost, user = user)
        productRepository.save(product)
        return "Product ${request.name} created successfully for user ${user.username}!"
    }

    @PutMapping("/{productId}")
    fun updateProduct(
        @RequestBody request: CreateProductRequest,
        @PathVariable userId: Long,
        @PathVariable productId: Long
    ): String {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("Invalid user Id:$userId") }
        val product = productRepository.findById(productId).orElseThrow {
            IllegalArgumentException("Invalid product Id:$productId") }

        product.name = request.name
        product.cost = request.cost
        productRepository.save(product)
        return "Product ${request.name} updated successfully for user ${user.username}!"
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(
        @PathVariable userId: Long,
        @PathVariable productId: Long
    ): String {
        val product = productRepository.findById(productId).orElseThrow {
            IllegalArgumentException("Invalid product Id:$productId") }
        productRepository.delete(product)
        return "Product deleted successfully!"
    }
}

