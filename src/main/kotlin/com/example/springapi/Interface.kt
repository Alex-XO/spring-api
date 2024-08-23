package com.example.springapi

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Role?
}

interface ProductRepository : JpaRepository<Product, Long> {
    fun findByUser(user: User): List<Product>
}

interface TagRepository : JpaRepository<Tag, Long> {
    fun findByName(name: String): Tag?
}
