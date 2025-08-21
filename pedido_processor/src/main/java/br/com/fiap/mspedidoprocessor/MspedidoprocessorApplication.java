package br.com.fiap.mspedidoprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "br.com.fiap.mspedidoprocessor.adapter.external")
public class MspedidoprocessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MspedidoprocessorApplication.class, args);
	}

}
