# GUIÓN DEL PROYECTO 

Este proyecto sirve de apoyo para la impartición del apartado **_Spring JDBC_** del módulo de **_BBDD_**
del curso **_Programación en JAVA_**.

En este documento se indican los pasos a seguir y, cuando procede, los comentarios y/o anotaciones a realizar en el 
transcurso de la clase.

Cada punto enumerado como **_Paso_**, se corresponde con un commit del proyecto.
1. ### Paso 1 - Explicación de cómo se crea un proyecto para Spring.
   * Desde spring [initializr](https://start.spring.io/index.html).
   * Desde el IDE de IntelliJ.
   - NOTA: Proyecto Maven en Spring 3.4.5 (o la última versión estable) con las dependencias:
     * Spring Boot Dev Tools.
     * Lombok.
     * JDBC API.
     * PostgreSQL Driver.
   * Se asigna la url del repositorio remoto.
2. ### Paso 2 -  Configuración del driver, activación de logs y obtención de pool utilizado por Spring.
    * Para evitar usar sout en cada mensaje de comprobación que queramos visualizar en la consola, utilizaremos 
   un _logger_. Spring incluye SLF4J por defecto (pero con una implementación básica).
    - NOTA: Para obtener el pool que utiliza Spring, necesitamos saber que tipo de _DataSource_ utiliza la aplicación.
   _Datasource_ es una interfaz, por lo que, lo que realmente queremos saber es qué implementación incluye Spring de 
   esa interfaz.
    - Para ello obtenemos el contexto de la aplicación devuelto por `SpringApplication.run`, y el nombre de la clase 
   del bean _DataSource.class_. Con la versión de Spring 3.4.5, debería ser _HikariDataSource_. 
    
3. 
   
    