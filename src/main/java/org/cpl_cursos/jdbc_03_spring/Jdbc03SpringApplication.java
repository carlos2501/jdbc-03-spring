package org.cpl_cursos.jdbc_03_spring;

import org.cpl_cursos.jdbc_03_spring.entidades.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;

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
		// --- Ejemplo de Inserción y recuperación de clave primaria ---
		Cliente nuevoCliente = new Cliente(
				null, // El codigo_cliente es null porque será generado por la BD
				"Nuevo Cliente Ejemplo",
				"Juan",
				"Pérez",
				"912345678",
				"918765432",
				"Calle Falsa 123",
				null, // linea_direccion2 puede ser null
				"Ciudad Ejemplo",
				"Región Ejemplo",
				"País Ejemplo",
				"28080",
				10,   // codigo_rep_ventas (asegúrate que existe si hay FK)
				5000.0F // limite_credito
		);
		Cliente clienteInsertado = insertarClienteYObtenerId(nuevoCliente);
		if (clienteInsertado != null && clienteInsertado.getCodigo_cliente() != null) {
			log.info("Cliente insertado con éxito. ID generado: {}", clienteInsertado.getCodigo_cliente());
			log.info("Datos del cliente insertado: {}", clienteInsertado);
		} else {
			log.error("No se pudo insertar el cliente o recuperar su ID.");
		}

	}

	/**
	 * Inserta un nuevo cliente en la base de datos y recupera el ID generado.
	 * @param cliente El objeto Cliente a insertar (sin codigo_cliente).
	 * @return El objeto Cliente actualizado con el codigo_cliente generado, o null si falla.
	 */
	private Cliente insertarClienteYObtenerId(final Cliente cliente) {
		// SQL con placeholders para todas las columnas excepto la PK autogenerada
		final String INSERT_SQL = "INSERT INTO cliente (nombre_cliente, nombre_contacto, apellido_contacto, " +
				"telefono, fax, linea_direccion1, linea_direccion2, ciudad, region, pais, " +
				"codigo_postal, codigo_empleado_rep_ventas, limite_credito) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // clavesGeneradas almacena las claves generadas [2, 3, 5]
		KeyHolder clavesGeneradas = new GeneratedKeyHolder();

		try {
			int filasAfectadas = temp.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					// Preparamos el statement indicando que queremos las claves generadas [1, 3, 4]
					PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"codigo_cliente"});

					// Asignamos los valores a los placeholders (?)
					ps.setString(1, cliente.getNombre_cliente());
					ps.setString(2, cliente.getNombre_contacto());
					ps.setString(3, cliente.getApellido_contacto());
					ps.setString(4, cliente.getTelefono());
					ps.setString(5, cliente.getFax());
					ps.setString(6, cliente.getLinea_direccion1());
					ps.setString(7, cliente.getLinea_direccion2()); // setString maneja nulls correctamente
					ps.setString(8, cliente.getCiudad());
					ps.setString(9, cliente.getRegion());         // setString maneja nulls correctamente
					ps.setString(10, cliente.getPais());
					ps.setString(11, cliente.getCodigo_postal());

					// Para tipos numéricos objeto (Integer, Double), usar setObject es más seguro para manejar nulls
					// o hacer una comprobación explícita
					if (cliente.getCodigo_rep_ventas() != null) {
						ps.setInt(12, cliente.getCodigo_rep_ventas());
					} else {
						ps.setNull(12, Types.INTEGER); // Especifica el tipo SQL si es null
					}

					if (cliente.getLimite_credito() != null) {
						ps.setDouble(13, cliente.getLimite_credito());
					} else {
						ps.setNull(13, Types.DOUBLE); // Especifica el tipo SQL si es null
					}

					return ps;
				}
			}, clavesGeneradas); // Pasamos el keyHolder al método update

			if (filasAfectadas > 0) {
				// Recuperamos la clave generada del KeyHolder [2, 3, 5]
				// getKey() devuelve Number. Si hay varias claves (raro en insert simple), usar getKeys()
				Number generatedKey = clavesGeneradas.getKey();
				if (generatedKey != null) {
					log.info("Inserción exitosa, {} fila(s) afectada(s).", filasAfectadas);
					// Actualizamos el objeto original con el ID obtenido
					cliente.setCodigo_cliente(generatedKey.intValue()); // o longValue() si tu PK es BIGSERIAL/BIGINT
					return cliente;
				} else {
					log.error("La inserción fue exitosa ({} fila(s)) pero no se pudo recuperar la clave generada.", filasAfectadas);
					// Podrías lanzar una excepción aquí si la clave es indispensable
					return null; // O devolver el cliente sin ID, o lanzar excepción
				}
			} else {
				log.warn("La inserción no afectó ninguna fila.");
				return null;
			}

		} catch (DataAccessException e) {
			log.error("Error al insertar el cliente: ", e);
			return null; // O relanzar una excepción específica
		}
	}
}
