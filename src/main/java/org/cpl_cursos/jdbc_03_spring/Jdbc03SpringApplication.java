package org.cpl_cursos.jdbc_03_spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Jdbc03SpringApplication {
	// Activamos el logger
	private static final Logger log = LoggerFactory.getLogger(Jdbc03SpringApplication.class);

    public static void main(String[] args) {
		/**
		 * 	SpringApplication.run devuelve un objeto de la clase ConfigurableApplicationContext que contiene el contexto
		 * 	de la aplicación.
		 * 	Obtenemos JdbcTemplate del contexto mediante el bean <JdbcTemplate.class>
		 */
		ConfigurableApplicationContext contexto = SpringApplication.run(Jdbc03SpringApplication.class, args);
        JdbcTemplate temp = contexto.getBean(JdbcTemplate.class);

		// ejecutamos una consulta para probar la conexión
		log.info("El último cliente añadido tiene el código: " + temp.queryForObject("SELECT MAX(codigo_cliente) FROM cliente", Integer.class));

	}

}
