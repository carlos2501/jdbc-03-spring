package org.cpl_cursos.jdbc_03_spring;

import org.cpl_cursos.jdbc_03_spring.entidades.Cliente;
import org.cpl_cursos.jdbc_03_spring.mapeadores.ClienteMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Jdbc03SpringApplication implements ApplicationRunner {
	@Autowired
	private JdbcTemplate temp;

	// Activamos el logger
	private static final Logger log = LoggerFactory.getLogger(Jdbc03SpringApplication.class);

    public static void main(String[] args) {
		SpringApplication.run(Jdbc03SpringApplication.class, args);
	}

	public void run(ApplicationArguments args) {
		// Consulta SQL
		String qry = "SELECT * FROM cliente WHERE codigo_cliente = ?";
		//
        // Clase para asignar los valores de la consulta
        Cliente cliente = temp.queryForObject(qry, new ClienteMap(), 3);
        assert cliente != null;
        log.info("Los datos del cliente son {}", cliente);
	}
}
