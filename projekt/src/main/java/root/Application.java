package root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static String clientId = "420058627236-1tkti7jdgtqu50ittq257f69dm8v5o3f.apps.googleusercontent.com";
    public static String clientSecret = "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";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
