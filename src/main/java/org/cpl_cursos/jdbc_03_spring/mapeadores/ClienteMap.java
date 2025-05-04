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

        // 2. Extrae los valores de cada columna del ResultSet (rs).
        //    Es crucial usar el nombre exacto (o alias) de la columna
        //    tal como viene en la consulta SQL.
        cliente.setCodigo_cliente(rs.getInt("codigo_cliente"));
        cliente.setNombre_cliente(rs.getString("nombre_cliente"));
        cliente.setNombre_contacto(rs.getString("nombre_contacto"));
        cliente.setApellido_contacto(rs.getString("apellido_contacto"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setFax(rs.getString("fax"));
        cliente.setLinea_direccion1(rs.getString("linea_direccion1"));
        cliente.setLinea_direccion2(rs.getString("linea_direccion2")); // Manejará NULL si la columna es nullable
        cliente.setCiudad(rs.getString("ciudad"));
        cliente.setRegion(rs.getString("region")); // Manejará NULL
        cliente.setPais(rs.getString("pais"));
        cliente.setCodigo_postal(rs.getString("codigo_postal"));

        // Para tipos numéricos objeto (Integer, Double), es mejor usar getObject
        // para manejar correctamente los valores NULL de la base de datos.
        // Si usas getInt/getDouble y el valor es NULL, devolverá 0, lo cual
        // podría no ser lo que deseas. getObject devolverá null.
        cliente.setCodigo_rep_ventas(rs.getObject("codigo_rep_ventas", Integer.class));
        cliente.setLimite_credito(rs.getObject("limite_credito", Double.class));

        // 3. Devuelve el objeto Cliente poblado.
        return cliente;
    }
}
