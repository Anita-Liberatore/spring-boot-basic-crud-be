package com.dev.talking.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
	

	
	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
//        StudentService ss = context.getBean(StudentService.class);
//        
//        StudentRepository sr = context.getBean(StudentRepository.class);
//        System.out.println(sr.toString());
        

	}
	
	
//	@GetMapping
//	public List<Student> getStudents() {
//		return List.of(
//				new Student(1L, 
//						"Anita", 
//						LocalDate.of(2000, Month.FEBRUARY, 15),
//						25));
//	}

}
