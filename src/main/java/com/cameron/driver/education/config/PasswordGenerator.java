package com.cameron.driver.education.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String password = "Ekj@t";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		System.out.println("The generated password is "+hashedPassword);
		String test ="$2a$10$cEHlHW5w3ZycmZ5YdF1LsuVewmtnsIKujfD5.mOHhSQ5yM4B5.VaK";
	}

}
