# GUIÓN DEL PROYECTO 

Este proyecto sirve de apoyo para la impartición del apartado **_Spring JDBC_** del módulo de **_BBDD_**
del curso **_Programación en JAVA_**.

En este documento se indican los pasos a seguir y, cuando procede, los comentarios y/o anotaciones a realizar en el 
transcurso de la clase.

Cada punto enumerado como **_Paso_**, se corresponde con un commit del proyecto.

### 1.  Paso 1 - Explicación de cómo se crea un proyecto para Spring.
   * Desde spring [initializr](https://start.spring.io/index.html).
   * Desde el IDE de IntelliJ.
   - NOTA: Proyecto Maven en Spring 3.4.5 (o la última versión estable) con las dependencias:
     * Spring Boot Dev Tools.
     * Lombok.
     * JDBC API.
     * PostgreSQL Driver.
   * Se asigna la url del repositorio remoto.

### 2.  Paso 2 -  Configuración del driver, activación de logs y obtención de pool utilizado por Spring.
* Para evitar usar sout en cada mensaje de comprobación que queramos visualizar en la consola, utilizaremos 
   un _logger_. Spring incluye SLF4J por defecto (pero con una implementación básica).

* NOTA: Para obtener el pool que utiliza Spring, necesitamos saber que tipo de _DataSource_ utiliza la aplicación.
   _Datasource_ es una interfaz, por lo que, lo que realmente queremos saber es qué implementación incluye Spring de 
   esa interfaz. Para ello obtenemos el contexto de la aplicación devuelto por `SpringApplication.run`, y el nombre de la clase 
   del bean _DataSource.class_. Con la versión de Spring 3.4.5, debería ser _HikariDataSource_.


* **NOTA: Para ver las implementaciones disponibles de _DataSource_, hacer clic con el botón derecho en _DataSource_ 
 y seleccionar <GO TO> e <Implementation(s)>**

### 3. Paso 3 - Uso de la clase JdbcTemplate y prueba con una consulta simple.
   * NOTA: Consultar la ficha resumen para ver en qué consiste y un par de formas de acceder a ella. Cada commit de
   este paso, usa una forma de inyección distinta para ver cómo se codifica.
     1. Creando una variable local (como hicimos en el paso anterior) y obteniéndo `JdbcTemplate` del contexto.
     2. Inyectando JdbcTemplate mediante la anotación @Autowired. Este modo de inyección no permite usar una variable 
     local como antes ya que debería ser estática (el método `main` es estático) y esto significa que debe estar 
     disponible en el momento de crear la clase, pero `JdbcTemplate` no se ha creado todavía, por lo que su valor es 
     `null`. Por eso, es necesario utilizar un método que se ejecute fuera del contexto estático y para implementamos
     la interfaz `ApplicationRunner` que permite ejecutar su método run inmediatamente después de crear la clase. De
     esta forma, podemos usar la variable que contiene nuestra template.
     
### 4. Paso 4 - Consulta simple de inserción. 
* El método `update` de JdbcTemplate admite un cadena -consulta SQL- y un número variable de objetos como parámetros.

**NOTA**: Nótese que no se incluyen los valores, ya que se enviarán como parámetros a JdbCTemplate)

### 5. Paso 5 - Consulta con parámetros con nombre.
* Necesitamos una consulta con parámetros para comprobar si la oficina se ha añadido. Utilizamos parámetros con nombre 
(en lugar de posición como en el paso anterior).
* Para poder utilizar parámetros con nombre, necesitamos usar otra _template_ proporcionada por Spring JDBC API llamada
`NamedParameterJdbcTemplate`, que encapsula JdbcTemplate y proporciona una forma alternativa de especificar los 
parámetros.
* Internamente, substituye los parámetros con nombre por sus correspondientes posicionadores `?` y delega la ejecución 
de la consulta a la `JdbcTemplate` encapsulada.
* Para usar esta nueva _template_, hay que inyectarla (mediante @Autowired o mediante la creación de una variable, 
como se vio anteriormente). 

* NOTA: En la línea 48 de `Jdbc03SpringApplication.java`, al escribir el nombre de la variable 
`namedParameterJdbcTemplate.query...`, el IDE de IntelliJ 
habrá creado automáticamente la sentencia @Autowired correspondiente (líneas 23 y 24) para inyectarla

### 6. Paso 6 y 7 - Asignación de resultados a objetos (_Mapeo_): clase RowMapper
* RowMapper es una interfaz que, mediante la implementación de sus métodos, permite asignar automáticamente los 
resultados de una consulta a objetos.
* Ponemos la clase que _mapea_ la consulta a la clase dentro de un nuevo paquete que denominamos "mapeadores".
Esto no es estrictamente necesario, pero nos ayuda a mantener el código organizado, mejorando su legibilidad. 
Más adelante nos facilitará adaptar nuestro código a la arquitectura MVC.

1. Creamos una clase para representar la tabla cliente dentro de un nuevo paquete "`entidades`".
   * NOTA: Usamos Lombok para getters, setters y constructores.
2. Implementamos la interfaz `RowMap<t>` en la clase `ClienteMap` para asignar los resultados de una consulta a la 
nueva clase `Cliente`.
3. Utilizamos la clase _mapeadora_ para poblar una instancia de Cliente con los resultados de una consulta.

### 7. Paso 8 - Ejemplo de consulta de lista registros.

### 8. Paso 9 y 10 - Consulta de inserción de un registro y obtención de la clave primaria insertada.
* Ahora se trata de insertar u nuevo registro y obtener la clave primaria del nuevo registro.
* Esto tiene sentido cuando la PK es generada por la BBDD (campo de tipo Autoincremental)
* **NOTA** Es necesario modificar la tabla cliente para hacer el campo `codigo_cliente` autoincremental. Para no 
eliminarlos registros existentes, hay que modificar las propiedades del campo desde un gestos de BBDD.
* NOTA Aunque se produce un error, se produce la inserción.
#### Análisis del Error: `GeneratedKeyHolder` Devuelve Múltiples Claves
#### El Problema
Se lanza la siguiente excepción al intentar recuperar una clave primaria generada después de una inserción usando 
`JdbcTemplate` de Spring y `GeneratedKeyHolder`:

> The getKey method should only be used when a single key is returned.
> The current key entry contains multiple keys:
> ```
> [{codigo_cliente=202, nombre_cliente=..., ...}]
> ```

#### Explicación

Este mensaje de error indica exactamente lo que está pasando:

1.  Se llamó al método `keyHolder.getKey()`.
2.  Este método está diseñado para funcionar **solo** cuando la base de datos devuelve *un único valor* como 
clave generada (el caso más común: una sola columna de clave primaria numérica).
3.  Sin embargo, en este caso, el driver JDBC o la configuración de la base de datos está devolviendo no solo el 
`codigo_cliente`, ¡sino una estructura (un `Map` dentro de una `List`) que contiene *múltiples columnas* de la 
fila insertada! (`codigo_cliente`, `nombre_cliente`, etc.).
4.  Como `getKey()` no sabe cuál de esas columnas es la *verdadera* clave que buscamos, lanza la excepción 
`InvalidDataAccessApiUsageException` para señalar esta ambigüedad.

#### Causa Principal

La causa más probable es que, al usar `Statement.RETURN_GENERATED_KEYS` durante la creación del `PreparedStatement`,
**no le especificamos explícitamente a Spring/JDBC cuál de las columnas** de tu tabla `cliente` es la que contiene la 
clave generada.

Cuando solo se proporciona la bandera `Statement.RETURN_GENERATED_KEYS`, algunos drivers JDBC (como el de PostgreSQL 
en ciertas configuraciones) pueden devolver más datos que solo la(s) columna(s) de la clave primaria designada. Esto 
podría incluir varias columnas o incluso parecerse a toda la fila insertada. 

Esta falta de especificidad crea ambigüedad para el `GeneratedKeyHolder`, lo que le lleva a almacenar múltiples pares 
clave-valor en lugar de solo la clave primaria.

#### Solución

La forma más directa y recomendada de solucionar esto es **indicar explícitamente el nombre de la columna (o columnas) 
que contienen las claves generadas** al crear el `PreparedStatement`.

Modificamos la línea donde creamos el `PreparedStatement` dentro de `PreparedStatementCreator`

**En lugar de esto:** `Statement.RETURN_GENERATED_KEYS`
**ponemos esto**: `new String[]{"codigo_cliente"}` 

