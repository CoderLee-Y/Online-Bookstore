
package org.lee.wssample;

import org.org.lee.introduction.GetSearchResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConsumingWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumingWebServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner lookup(BookClient quoteClient) {
		return args -> {
			String text = "Java";

			if (args.length > 0) {
				text = args[0];
			}
			GetSearchResponse response = quoteClient.getBook(text);
			System.out.println(response.getIntros().toString());
		};
	}

}
