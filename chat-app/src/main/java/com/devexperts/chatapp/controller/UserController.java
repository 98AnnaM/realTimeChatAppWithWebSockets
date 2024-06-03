package com.devexperts.chatapp.controller;

import com.devexperts.chatapp.service.JwtService;
import com.devexperts.chatapp.model.dto.UserSearchDto;
import com.devexperts.chatapp.model.dto.UserViewAndEditDto;
import com.devexperts.chatapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/report")
    public String getRecords(Model model) {
        List<UserViewAndEditDto> users = userService.getAll();
        model.addAttribute("users", users);
        model.addAttribute("usersNumber", users.size());
        return "report";
    }

    @PreAuthorize("(isAuthenticated() && (hasRole('ADMIN') or #userId == authentication.principal.id))")
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long userId) {
        log.info("Deleting user with ID: {}", userId);
        userService.deleteUser(userId);
        log.info("User with ID: {} was successfully deleted.", userId);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated() && #userId == authentication.principal.id")
    @GetMapping("/edit/{id}")
    public String editUser(
            @PathVariable("id") Long userId,
            Model model) {

        if (!model.containsAttribute("editUserDto")) {
            UserViewAndEditDto editUserDto = userService.getUserEditDetails(userId);
            model.addAttribute("editUserDto", editUserDto);
        }
        return "edit";
    }

    @PutMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long userId,
                             @Valid UserViewAndEditDto editUserDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             HttpServletResponse response) {

        log.info("Editing user with ID: {}", userId);

        final UserViewAndEditDto currentUser = userService.getUserEditDetails(userId);

        if (!editUserDto.getUsername().equals(currentUser.getUsername()) &&
                userService.usernameExists(editUserDto.getUsername())) {
            bindingResult.addError(new FieldError("editUserDto", "username", editUserDto.getUsername(), false, null, null,
                    "This username is occupied!"));
        }

        if (!editUserDto.getEmail().equals(currentUser.getEmail()) &&
                userService.emailExists(editUserDto.getEmail())) {
            bindingResult.addError(new FieldError("editUserDto", "email", editUserDto.getEmail(), false, null, null,
                    "This email is occupied!"));
        }

        if (bindingResult.hasErrors()) {
            log.error("Editing failed for user with ID: {}", userId);
            logFieldErrors(bindingResult.getFieldErrors());

            redirectAttributes.addFlashAttribute("editUserDto", editUserDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editUserDto", bindingResult);
            return "redirect:/edit/" + userId;
        }

        userService.updateUser(editUserDto, userId);
        response.addCookie(createNewToken(editUserDto.getUsername()));
        log.info("Successfully edited user with ID: {}", userId);
        return "redirect:/report";
    }

    @GetMapping("/search")
    public String searchQuery(@RequestParam Map<String, String> queryParams,
                              HttpServletRequest request,
                              @Valid UserSearchDto searchUserDto,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("searchUserDto", searchUserDto);
            model.addAttribute(
                    "org.springframework.validation.BindingResult.searchUserDto",
                    bindingResult);
            return "redirect:/search";
        }

        if (!model.containsAttribute("searchUserDto")) {
            model.addAttribute("searchUserDto", searchUserDto);
            model.addAttribute("criteria", searchUserDto.toString());
        }

        if (!searchUserDto.isEmpty()) {
            model.addAttribute("users", userService.searchUsers(searchUserDto));
            model.addAttribute("criteria", searchUserDto.toString());
        }
        return "search";
    }

    @GetMapping("/under18")
    public String getUsersUnder18(Model model) {
        List<UserViewAndEditDto> usersUnder18 = userService.getUsersUnder18();
        model.addAttribute("users", usersUnder18);
        model.addAttribute("usersNumber", usersUnder18.size());
        model.addAttribute("title", "under 18 years old");
        return "under18";
    }

    @GetMapping("/fromBulgaria")
    public String getUsersFromBulgaria(Model model) {
        List<UserViewAndEditDto> usersFromBulgaria = userService.getUsersFromBulgaria();
        model.addAttribute("users", usersFromBulgaria);
        model.addAttribute("usersNumber", usersFromBulgaria.size());
        model.addAttribute("title", "from Bulgaria");
        return "fromBulgaria";
    }

    private void logFieldErrors(List<FieldError> fieldErrors) {
        for (FieldError error : fieldErrors) {
            log.error("Field error: field={}, rejectedValue={}, message={}",
                    error.getField(), error.getRejectedValue(), error.getDefaultMessage());
        }
    }

    private Cookie createNewToken(String username) {
        String token = jwtService.generateToken(username);
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        Instant expirationTime = Instant.now().plus(Duration.ofDays(1));
        cookie.setMaxAge(Math.toIntExact(Duration.between(Instant.now(), expirationTime).getSeconds()));
        return cookie;
    }
}
