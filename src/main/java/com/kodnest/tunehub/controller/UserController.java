package com.kodnest.tunehub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kodnest.tunehub.entity.Song;
import com.kodnest.tunehub.entity.User;
import com.kodnest.tunehub.service.SongService;
import com.kodnest.tunehub.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	SongService songService;

	@PostMapping("/register")
	// @RequestParam("username") String username, @RequestParam("email") String
	// email,@RequestParam("password") String password, @RequestParam("gender")
	// String gender,@RequestParam("role") String role,@RequestParam("address")
	// String address
	public String addUser(@ModelAttribute User user) {

		// Email taken from the registration form
		String email = user.getEmail();
		// Checking if email entered is present in database or not
		boolean status = userService.emailExists(email);
		if (!status) {
			userService.addUser(user);
			System.out.println("User added");
		} else {
			System.out.println("User already exists");
			return "login";
		}
		return "login";

	}

	// validating login of the user (if the user is a valid user or not)
	@PostMapping("/validate")
	public String validate(@RequestParam("email") String email,
			@RequestParam("password") String password, HttpSession session, Model model) {

		if(userService.validateUser(email, password) == true){

			String role = userService.getRole(email);

			session.setAttribute("email", email);

			if(role.equals("admin")) {

				return "adminhome";
			}
			else {
				User user = userService.getUser(email);
				boolean userstatus = user.isIspremium();
				List<Song> songList = songService.fetchAllSongs();
				model.addAttribute("songs", songList);
				model.addAttribute("ispremium", userstatus);
				return "customerhome";
			}
		}	
		else {
			return "login";
		}	 
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
	
	@GetMapping("/forgotPassword")
	public String showForgotPasswordForm() {
	    return "forgotPassword"; // Assuming you have a Thymeleaf template for the forgot password page
	}
	
	@PostMapping("/sendResetLink")
	public String sendResetLink(@RequestParam("email") String email, Model model) {
	    boolean emailExists = userService.emailExists(email);
	    if (emailExists) {
	        // Logic to send reset link to the provided email address
	        // This could involve generating a unique token and sending an email with a link containing this token
	        // Once the link is clicked, the user will be directed to a page to reset their password
	        // For simplicity, we'll just return a success message for now
	        model.addAttribute("message", "Password reset link sent successfully. Please check your email.");
	        return "forgotPasswordSuccess"; // Assuming you have a Thymeleaf template for the password reset success page
	    } else {
	        // Email does not exist, show an alert message
	        model.addAttribute("errorMessage", "The provided email does not exist.");
	        return "forgotPassword"; // Assuming you have a Thymeleaf template for the forgot password page
	    }
	}

}
//	@PostMapping("/validate")
//	public String validate(@RequestParam("email") String email, @RequestParam("password") String password,
//			HttpSession session) {
//		if (userService.validateUser(email, password) == true) {
//			String role = userService.getRole(email);
//			session.setAttribute("email", email);
//			if (role.equals("admin")) {
//				System.out.println("Valid user->Home Page");
//				return "adminhome";
//			} else {
//				return "customerhome";
//			}
//		} else {
//			return "login";
//		}
//	}


//	@GetMapping("/validate")
//	public String validate(@ModelAttribute User user) {
//		
//		if(!userService.userExists(user.getEmail(), user.getPassword())) {
//			return "registration";			
//		}
//		return "home";
//	}
