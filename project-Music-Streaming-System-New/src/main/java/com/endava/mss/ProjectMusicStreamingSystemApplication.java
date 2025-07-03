package com.endava.mss;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableAspectJAutoProxy
public class ProjectMusicStreamingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectMusicStreamingSystemApplication.class, args);
	} 

}
 