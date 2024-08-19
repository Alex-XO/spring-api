package com.example.springapi

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class UserController(private val userRepository: UserRepository, private val roleRepository: RoleRepository) {

    @GetMapping("/")
    fun home(model: Model): String {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val currentPrincipalName: String = authentication.name
        val currentUser = userRepository.findByUsername(currentPrincipalName)

        model.addAttribute("users", userRepository.findAll())
        model.addAttribute("currentUser", currentUser)
        return "user_list"
    }

    @GetMapping("/users/new")
    fun showAddUserForm(model: Model): String {
        model.addAttribute("roles", roleRepository.findAll())
        return "add_user"
    }

    @PostMapping("/users")
    fun addUser(@RequestParam username: String, @RequestParam password: String, @RequestParam role: String): String {
        val encodedPassword = BCryptPasswordEncoder().encode(password)
        val userRole = roleRepository.findByName(role) ?: throw IllegalArgumentException("Invalid role")
        val user = User(username = username, password = encodedPassword, enabled = true, role = userRole)
        userRepository.save(user)
        return "redirect:/"
    }

    @GetMapping("/users/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun showEditUserForm(@PathVariable id: Long, model: Model): String {
        val user = userRepository.findById(id).orElseThrow { IllegalArgumentException("Invalid user Id:$id") }
        model.addAttribute("user", user)
        model.addAttribute("roles", roleRepository.findAll())
        return "edit_user"
    }

    @PostMapping("/users/update")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateUser(@RequestParam id: Long, @RequestParam username: String, @RequestParam password: String, @RequestParam role: String): String {
        val user = userRepository.findById(id).orElseThrow { IllegalArgumentException("Invalid user Id:$id") }
        val encodedPassword = BCryptPasswordEncoder().encode(password)
        val userRole = roleRepository.findByName(role) ?: throw IllegalArgumentException("Invalid role")
        user.username = username
        user.password = encodedPassword
        user.role = userRole
        userRepository.save(user)
        return "redirect:/"
    }

    @GetMapping("/users/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(@PathVariable id: Long): String {
        userRepository.deleteById(id)
        return "redirect:/"
    }
}