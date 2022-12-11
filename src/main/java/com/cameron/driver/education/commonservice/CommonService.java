package com.cameron.driver.education.commonservice;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CommonService {

	public static String formatDate(Date date) {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String dateStr = simpleDateFormat.format(date);
		return dateStr;
	}
	public static String formatDateTime(Date date) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String dateStr = simpleDateFormat.format(date);
		return dateStr;
	}
	
	public static String getUsername() {
		  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		  System.out.println(authentication.getAuthorities());
	        return authentication.getName();
	}

	public static String getUserRole() {
		  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		  String role=null;
		  if(authentication.getAuthorities().iterator().hasNext()) {
			  role=authentication.getAuthorities().iterator().next().getAuthority();
		  }
		  return role;
	}
}

