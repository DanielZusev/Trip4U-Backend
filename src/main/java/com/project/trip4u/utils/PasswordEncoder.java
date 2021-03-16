package com.project.trip4u.utils;

public class PasswordEncoder {

	private final String KEY = "1dy68sef6a4mhhj8k12t49nk23gj68";

	public PasswordEncoder() {

	}

	public String encode(String email, String password) { // plain text password & plain text email
		String encodedPassword = "";
		String key = KEY;
		int lengthDifferences = 0;
		int emailLength = email.length();
		int passwordLength = password.length();

		if (emailLength != passwordLength) {

			lengthDifferences = Math.abs(emailLength - passwordLength);
			while (lengthDifferences > key.length()) {
				key += KEY;
			}

			if (emailLength > passwordLength) { // EMAIL IS LONGER

				for (int i = 0; i < lengthDifferences; i++) {
					password += key.charAt(i);
				}

			} else { // PASSWORD IS LONGER

				for (int i = 0; i < lengthDifferences; i++) {
					email += key.charAt(i);
				}
			}
		}

		for (int i = 0; i < password.length(); i++) {
			encodedPassword += password.charAt(i) ^ email.charAt(i);
		}
		
		return encodedPassword;
	}

	public boolean match(String email, String originPassword, String dbPassword) { // plain text email & password from user & password from db
		
		return dbPassword.equals(encode(email, originPassword));
	}

}
