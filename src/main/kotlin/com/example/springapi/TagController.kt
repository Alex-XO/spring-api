package com.example.springapi

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/{userId}/products/{productId}/tags")
class TagController(
    private val productRepository: ProductRepository,
    private val tagRepository: TagRepository,
    private val userRepository: UserRepository
) {

    @PostMapping
    fun addTagsToProduct(
        @PathVariable userId: Long,
        @PathVariable productId: Long,
        @RequestBody tagsRequest: List<Map<String, String>>
    ): String {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("Invalid user Id: $userId") }
        val product = productRepository.findById(productId).orElseThrow { IllegalArgumentException("Invalid product Id: $productId") }

        if (product.user.id != user.id) {
            throw IllegalArgumentException("Product does not belong to the specified user")
        }

        tagsRequest.forEach { tagRequest ->
            val tagName = tagRequest["name"] ?: throw IllegalArgumentException("Tag name is required")
            var tag = tagRepository.findByName(tagName)

            if (tag == null) {
                tag = Tag(name = tagName)
                tagRepository.save(tag)
            }

            if (!product.tags.contains(tag)) {
                product.tags.add(tag)
            }
        }

        productRepository.save(product)

        return "Tags added to product '${product.name}' successfully!"
    }

    @DeleteMapping("/{tagId}")
    fun removeTagFromProduct(
        @PathVariable productId: Long,
        @PathVariable userId: Long,
        @PathVariable tagId: Long
    ): String {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("Invalid user Id: $userId") }

        val product = productRepository.findById(productId)
            .orElseThrow { IllegalArgumentException("Invalid product Id:$productId") }
        val tag = tagRepository.findById(tagId)
            .orElseThrow { IllegalArgumentException("Invalid tag Id:$tagId") }

        if (product.user.id != user.id) {
            throw IllegalArgumentException("Product does not belong to the specified user")
        }

        product.tags.remove(tag)
        productRepository.save(product)
        return "Tag removed from product successfully!"
    }
}
