package com.example.springapi

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Role?
}
