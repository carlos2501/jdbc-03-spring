package org.cpl_cursos.jdbc_03_spring;

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

	@Override
	public void run(ApplicationArguments args) {
		// para mayor claridad del código, separamos la consulta del mensaje en consola
		// ejecutamos una consulta para probar la conexión
		Integer ultCli = temp.queryForObject("SELECT MAX(codigo_cliente) FROM cliente", Integer.class);
		log.info("El último cliente añadido tiene el código: " + ultCli);
	}
}
