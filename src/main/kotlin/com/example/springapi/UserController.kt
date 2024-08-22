package com.example.springapi

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
    fun addUser(@RequestBody request: CreateUserRequest): String {
        val userRole = roleRepository.findByName(request.role) ?: throw IllegalArgumentException("Invalid role")
        val user = User(username = request.username, password = request.password, enabled = true, role = userRole)
        userRepository.save(user)
        return "User ${request.username} created successfully!"
    }

    @PutMapping("/users/{id}")
    fun updateUser(@RequestBody request: CreateUserRequest, @PathVariable id: Long): User {
        val user = userRepository.findById(id).orElseThrow { IllegalArgumentException("Invalid user Id:$id") }
        val userRole = roleRepository.findByName(request.role) ?: throw IllegalArgumentException("Invalid role")
        user.username = request.username
        user.password = request.password
        user.role = userRole
        userRepository.save(user)
        return user
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userRepository.deleteById(id)
    }
}