package kg.peaksoft.peaksoftlmsb6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class PeaksoftlmsB6Application {

	public static void main(String[] args) {
		SpringApplication.run(PeaksoftlmsB6Application.class, args);
		System.out.println("Welcome colleagues, project name is Peaksoft-LMS!");
	}

	@GetMapping("/")
	public String greetingPage(){
		return "welcome";
	}
}
