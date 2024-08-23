package com.example.springapi

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tags")
class TagManagementController(
    private val tagRepository: TagRepository
) {

    @PostMapping
    fun createTag(@RequestBody request: CreateTagRequest): Tag {
        val tag = Tag(name = request.name)
        return tagRepository.save(tag)
    }

    @DeleteMapping("/{tagId}")
    fun deleteTag(@PathVariable tagId: Long): String {
        val tag = tagRepository.findById(tagId)
            .orElseThrow { IllegalArgumentException("Invalid tag Id:$tagId") }
        tagRepository.delete(tag)
        return "Tag deleted successfully!"
    }
}