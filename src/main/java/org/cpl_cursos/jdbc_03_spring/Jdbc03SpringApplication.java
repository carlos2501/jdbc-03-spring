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

	public void run(ApplicationArguments args) {
		// Consulta SQL
		String qry = """
			INSERT INTO oficina (codigo_oficina, ciudad, pais, region, codigo_postal, telefono, linea_direccion1)
				VALUES (?,?,?,?,?,?,?)
			""";
		// Al consultar la firma de <update> se observa que devuelve un entero con el número de filas actualizadas
		int filas = temp.update(qry, "AGP-ES", "Málaga", "España", "Andalucía","29023", "951 567 432", "C/ Larios 26");
		log.info("Se han añadido " + filas + " oficinas");
	}
}
