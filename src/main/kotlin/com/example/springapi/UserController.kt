package com.example.springapi

import jakarta.websocket.server.PathParam
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository) {

    @GetMapping("/users")
    fun getUsers(): List<User> {
        return userRepository.findAll()
    }

    @PostMapping("/users")
    fun addUser(@RequestParam username: String, @RequestParam password: String, @RequestParam role: String): User {
        val userRole = roleRepository.findByName(role) ?: throw IllegalArgumentException("Invalid role")
        val user = User(username = username, password = password, enabled = true, role = userRole)
        userRepository.save(user)
        return user
    }

    @PutMapping("/users/{id}")
    fun updateUser(@PathVariable id: Long, @RequestParam username: String, @RequestParam password: String, @RequestParam role: String): User? {
        val user = userRepository.findById(id).orElseThrow { IllegalArgumentException("Invalid user Id:$id") }
        val userRole = roleRepository.findByName(role) ?: throw IllegalArgumentException("Invalid role")
        user.username = username
        user.password = password
        user.role = userRole
        userRepository.save(user)
        return user
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userRepository.deleteById(id)
    }
}