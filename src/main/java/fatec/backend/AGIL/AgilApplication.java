package fatec.backend.AGIL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgilApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgilApplication.class, args);
		System.out.println("Vamos ver..... está funcionando!!");
		System.out.println("É possível acessar os end-points e testalos em: http://localhost:8080/swagger-ui/index.html");
	}
}