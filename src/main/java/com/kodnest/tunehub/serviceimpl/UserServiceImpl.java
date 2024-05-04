package com.kodnest.tunehub.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodnest.tunehub.entity.User;
import com.kodnest.tunehub.repository.UserRepository;
import com.kodnest.tunehub.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	// Adding a new user to the database
	public String addUser(User user) {
		userRepository.save(user);
		return "User added successfully";
	}

	@Override
	// To check the duplicate entries
	public boolean emailExists(String email) {
		if (userRepository.findByEmail(email) != null) {
			return true;
		} else {
			System.out.println("Absent");
			return false;
		}
	}
//	Can be done in this way as well
//		public boolean emailExists(String email) {
//			boolean e= userRepository.existsByEmail(email);
//			return e;
//		}

	@Override
	public boolean validateUser(String email, String password) {
		User user=userRepository.findByEmail(email);
		String userPassword = user.getPassword();
		if (password.equals(userPassword)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getRole(String email) {
		User user=userRepository.findByEmail(email);
		return user.getRole();
	}

	@Override
	public User getUser(String email) {
		
		return userRepository.findByEmail(email);
	}

	@Override
	public void updateUser(User user) {
		userRepository.save(user);
	}

//	@Override
//	public boolean userExists(String email, String password) {
//		boolean e = userRepository.existsByEmail(email);
//		boolean p = userRepository.existsByPassword(password);
//		if (e) {
//			if (p) {
//				return true;
//			}
//		} else {
//			return false;
//		}
//		return p;
//	}
}
