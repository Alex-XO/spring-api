package com.example.springapi

import jakarta.persistence.*

@Entity
@Table(name = "tags")
data class Tag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,

    @ManyToMany(mappedBy = "tags")
    val products: MutableList<Product> = mutableListOf()
)

class CreateTagRequest(
    val name: String
)
