# Proyecto base para implementar arquitectura hexagonal (patron puertos y adaptadores)
Ejemplo básico: guardar y recuperar los datos de un usuario.

![Descripción de la imagen](https://github.com/estebanbri/scaffold-arquitectura-hexagonal/blob/master/arquitectura.png)

Como probar el ejemplo?

Get - http://localhost:8080/users/{userId}

Post - http://localhost:8080/users


## Pasos 1: Recolectar las reglas de negocio de la casuistica.
Identificando actores y tareas que hacen cada uno de ellos.

Ejemplo:
- Yo necesito poder guardar los datos de un usuario
- Yo necesito poder recuperar los datos de un usuario dado un id

## Pasos 2: Crear el paquete de 'domain'.
Crear objetos para representar los modelos identificados del paso 1 (models, enums,...).

## Pasos 3: Crear el paquete 'application' 
En este paso volcar las reglas de negocio recolectadas en el paso 1 a puertos, es decir a especificaciones (spec) de
tu sistema, es decir en este punto por ahora no pensamos en la implementación solo en especificación mediante interfaces.

### Paso 3.1: Crear puertos primarios (inbound)
Es la "spec core" de lo que necesita hacer tu app para cumplir lo anterior. Tambien se los conoce como inbound
o de entrada porque son puertos de entrypoint a la logica central de tu app.
```
bff-tienda/application/port/in
```
```java
public interface AdministrarUsuario {
    Usuario guardar(Usuario user);
    Usuario retornarPorId(Long userId);
}
```
### Paso 3.2:  Crear puertos secundarios (outbound)
A diferencia de los puertos principales los puertos secundarios son "spec de soporte" para poder cumplir la casuistica
de la regla de negocio manera completa. Tambien se los conoce como outbound o de salida porque son puertos
de los cuales tu aplicación va a exponer para servicios externos como api's third party o db.
```
bff-tienda/application/port/out
```
Como su trabajo es simplemente guardar, y permitir recuperar usuarios entonces:

```java
public interface UsuarioRepositorio {
    Usuario guardar(Usuario usuario);
    Usuario retornarPorId(Long userId);
}
```

### Pasos 3.3: Implementar los puertos primarios en casos de usos (UseCases)
Dichas implementaciones van a implementar los puertos primarios y va a hacer uso de los puertos
secundarios mediante composición. (Asi se logra la independencia de la infra, y la infra es quien
se va a encargar de inyectar el bean)
```
bff-tienda/application/usecase
```

```java
@UseCase
@RequiredArgsConstructor
@Slf4j
public class AdministrarUsuarioUseCase implements AdministrarUsuario {

    private final UsuarioRepositorio usuarioRepositorio;

    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepositorio.guardar(usuario);
    }

    @Override
    public Usuario retornarPorId(Long userId) {
        return usuarioRepositorio.retornarPorId(userId);
    }
}
```

### Paso 3.4: Crear testing
- test unitarios 
- test de aceptación de la capa de application: usando los puertos primarios como puerta de entrada para el testing

## Paso 4: Crear el paquete infrastructure
En esta capa van a vivir los frameworks, dependencias a librerias externas, llamadas a api's third party, etc.
Como vas a notar cada adapter va a contener su propio mapper. Es decir este mappear se va a usar para mapear
entre el objeto del ambito del adapter (es decir el tipo de objeto que maneja el adapter
ej: si es jpa va a ser @Entity object o si es rest DTO object) al objeto de dominio y viceversa.

### Paso 4.1: Crear los adapters inbound.
Cada adapter inbound (interno) va a hacer uso mediante composición de los puertos primarios.
```java
@RequiredArgsConstructor
@RestController
public class RestUserAdapter {

    private final AdministrarUsuarioPort administrarUsuario;

    private final UsuarioDtoMapper usuarioMapper;

    @GetMapping("users/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(usuarioMapper.toDto(administrarUsuario.retornarPorId(id)), HttpStatus.OK);

    }

    @PostMapping("users")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(usuarioMapper.toDto(administrarUsuario.guardar(usuarioMapper.toDomain(userDto))),
                HttpStatus.CREATED);
    }

}
```
### Paso 4.2: Crear Testing
- test de integración del adaptador primario. (con @WebMvcTest)

### Paso 4.3: Crear los adapters outbound.
Cada adapter outbound (externo) va a implementar los puertos secundarios.

```java
@RequiredArgsConstructor
public class JpaUserAdapter implements UsuarioRepositorio {

    private final SpringJpaUsuarioRepository springJpaUsuarioRepository;

    private final UsuarioEntityMapper usuarioMapper;

    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioMapper.toDomain(springJpaUsuarioRepository.save(usuarioMapper.toEntity(usuario)));
    }

    @Override
    public Usuario retornarPorId(Long userId) {
        return usuarioMapper.toDomain(springJpaUsuarioRepository.findById(userId).orElseThrow());
    }
}
```

### Paso 4.4: Crear Testing
- test de integración del adaptador secundario. (con @DataJpaTest)


### Paso 5: Para que spring autoconfigure los beans de los casos de usos:

Al definir una estructura de paquetes custom spring no va a reconocer los path de componentes anotados.
Por ende tenes dos alternativas:
- Alternativa 1: Definir manualmente los beans en archivo de @Configuration
- Alternativa 2: Crear una anotacion custom (@UseCase) que sea un alias a @Component e indicarle a spring mediante

```java
@Configuration
@ComponentScan(
basePackages = "com.arhohuttunen.coffeeshop.application",
includeFilters = @ComponentScan.Filter(
type = FilterType.ANNOTATION, value = UseCase.class
)
)
public class ApplicationConfig {
}
```

# Conclusión
Como ves se comienza a construir desde el centro hacia afuera. Esto nos permite enfocarnos
en el dominio y la logica del negocio sin frenarnos en detalles de implementación como
librerias externas, bases de datos, etc.

Esto nos permite ir testeando por capas es decir, cuando tenes la capa de aplicación ya podes crear:
- Test unitarios.
- Test de aceptación: usando los puertos primarios (inbound) como puerta de entrada para el testing
Y cuando ya tenemos la capa infrastructure podés crear:
- Test de integración.

## Puertos primarios (inbound) versus Puertos secundarios (outbound)
Una cosa para notar es que no necesitamos de interfaces para los puertos primarios! 
Tener estas interfaces disponibles nos hace poder visualizar mas facil el rol de los puertos primarios, 
es decir tener estas interfaces en su lugar hace que sea más aparente cual es el boundary de nuestra aplicacion,
pero no son necesarias dichas interfaces. 
Unicamente necesitamos interfaces para los puertos secundarios para poder invertir las dependencias allí.

Ya que recorda que las interfaces con una unica implementacion son interfaces no utiles. 
Como nuestro core de la aplicacion no es algo que lo vamos a cambiar por otra implementacion, 
podemos solo exponer los metodos publicos de la implementacion concreta como puertos primarios.

LLevando esto a tu proyecto y aplicandolo, como podras ver la carpeta 'port' son los puertos secundarios
es decir ellos si llevan interfaces, pero para los puertos primarios no les creaste inferfaces directamente
exponemos los metodos publicos de los usecases.

## Multiples archivos para cada puerto o un archivo por puerto
Depende de preferencias y necesidades específicas del proyecto, es decir:
- con proyecto pequeño, o arquitectura no muy compleja esta ok tener un unico archivo con multiples puertos
- con proyecto grande, o arquitectura compleja tener cada puerto en su propio archivo escala más.

Fuente: 
- https://github.com/arhohuttunen/spring-boot-hexagonal-architecture/blob/main/coffeeshop-application
- https://github.com/yoandypv/spring-boot-clean-architecture
- https://github.com/refactorizando-web/spring-data-hexagonal-architecture
