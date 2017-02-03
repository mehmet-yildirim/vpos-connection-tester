package tr.com.innova.tester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VposConnectionTesterApplication {

	private static final String BANK_URL = "https://onlineodeme.vakifbank.com.tr:4443/VposService/v3/Vposreq.aspx";

	public static void main(String[] args) {
		SpringApplication.run(VposConnectionTesterApplication.class, args);
	}

	@Bean
	public VakifConnection vakifConnection() {
		return new VakifConnection(BANK_URL);
	}
}
