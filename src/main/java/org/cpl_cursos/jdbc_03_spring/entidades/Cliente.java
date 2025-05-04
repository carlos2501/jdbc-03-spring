package org.cpl_cursos.jdbc_03_spring.entidades;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cliente {
    private Integer codigo_cliente;     // <--Tipo Integer (Objeto wrapper)
    private String nombre_cliente;
    private String nombre_contacto;
    private String apellido_contacto;
    private String telefono;
    private String fax;
    private String linea_direccion1;
    private String linea_direccion2;
    private String ciudad;
    private String region;
    private String pais;
    private String codigo_postal;
    private Integer codigo_rep_ventas;
    private Float limite_credito;
}
