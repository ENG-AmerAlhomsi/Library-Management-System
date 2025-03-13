package com.project.library_management_system;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class LibraryManagementSystem {

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystem.class, args);
	}

}
