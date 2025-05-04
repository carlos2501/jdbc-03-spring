package org.cpl_cursos.jdbc_03_spring.mapeadores;

import org.cpl_cursos.jdbc_03_spring.entidades.Cliente;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteMap implements RowMapper<Cliente> {
    @Override
    public Cliente mapRow(ResultSet rs, int numFila) throws SQLException {
        // 1. Crea una nueva instancia de Cliente para cada fila.
        Cliente cliente = new Cliente();

        // 2. Extraemos los valores de cada columna del ResultSet usando los nombres de columna.
        // Usamos los métodos getXXX apropiados para cada tipo de dato.
        // Lombok (@Setter) nos proporciona los métodos setXXX.

        // Para tipos objeto como Integer y Double, usamos rs.getObject(columnName, Class)
        // para manejar correctamente los valores NULL de la base de datos.
        // Si la columna en la BD es NULL, el setter recibirá null.
        // Si usáramos rs.getInt() o rs.getDouble(), un NULL en la BD se convertiría en 0 o 0.0.
        cliente.setCodigo_cliente(rs.getObject("codigo_cliente", Integer.class));
        cliente.setNombre_cliente(rs.getString("nombre_cliente"));
        cliente.setNombre_contacto(rs.getString("nombre_contacto"));
        cliente.setApellido_contacto(rs.getString("apellido_contacto"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setFax(rs.getString("fax"));
        cliente.setLinea_direccion1(rs.getString("linea_direccion1"));
        cliente.setLinea_direccion2(rs.getString("linea_direccion2")); // Puede ser null
        cliente.setCiudad(rs.getString("ciudad"));
        cliente.setRegion(rs.getString("region"));               // Puede ser null
        cliente.setPais(rs.getString("pais"));
        cliente.setCodigo_postal(rs.getString("codigo_postal"));
        cliente.setCodigo_rep_ventas(rs.getObject("codigo_empleado_rep_ventas", Integer.class)); // Puede ser null
        cliente.setLimite_credito(rs.getObject("limite_credito", Float.class));     // Puede ser null

        // Devolvemos el objeto Cliente poblado.
        return cliente;
    }
}
