package com.javainuse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.javainuse.model.Employee;
import com.javainuse.service.EmployeeService;
import com.javainuse.service.impl.EmployeeServiceImpl;

public class SpringBootJdbcApplication extends Thread {

	public static String ParseStringURL(String input) {
		String result = null;
		String head = "//";
		String tail = "@";
		if (input != null) {
			if (input.length() > 1) {
				if (input.contains(head) && input.contains(tail)) {
					String subData = input.substring(input.indexOf(head) + 2, input.lastIndexOf(tail) + 1);
					result = input.replace(subData, "");
					System.out.println("result:   " + result);
				}
				return input;
			}
			return input;
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(ParseStringURL("qdfgkj"));
		//test("https://admin:netapp1!@10.224.10.24/rest/api/2");
	}

	
	
}