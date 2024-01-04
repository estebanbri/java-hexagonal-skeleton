# Proyecto base para implementar arquitectura hexagonal (patron puertos y adaptadores)
Ejemplo básico: guardar y recuperar los datos de un usuario.
Nota: hay un concepto de vertical slicing que mejora el diseño. Se basa en dividir tu app en features y cada feature va a contener 
su propias 3 capas de la hexagonal. (Mas sobre esto: https://medium.com/@oliveraluis11/arquitectura-hexagonal-con-spring-boot-parte-1-57b797eca69c)

![Descripción de la imagen](https://github.com/estebanbri/scaffold-arquitectura-hexagonal/blob/master/arquitectura.png)

> Totalmente prohibido: que codigo de tu capa de application (es decir tus usecases) acceda a clases/interfaces directamente que se encuentren definidos en la capa de infrastructure.
> Es decir la relacion de uso siempre es de afuera hacia adentro, como vemos en nuestro ejemplo nuestra clase @RestController que se encuentra en la capa infra puede usar los puertos primarios
> (o incluso los usecases directamente como detallo mas abajo) que se encuentran en la capa de application. Pero nunca podrias en tus clases de usecases que se encuentran en la capa de application
> usar clases/interfaces que se encuentran definidas en la capa de infra. De esto es lo que se basa la arquitectura hexa es decir los boundaries de la capa de aplicacion tanto flujos de entrada
> como de salida de la capa de application se hacen por medio de interfaces propias definidas dentro de la misma capa de application (que en la arquitectura estas interfaces son conocidas como puertos). 

Como probar el ejemplo?

Get - http://localhost:8080/users/{userId}

Post - http://localhost:8080/users


## Pasos 1: Recolectar las reglas de negocio de la casuistica.
Identificando actores y tareas que hacen cada uno de ellos.

Ejemplo:
- Yo necesito poder guardar los datos de un usuario, y poder registrar el alta en un sistema externo mediante llamada api rest.
- Yo necesito poder recuperar los datos de un usuario dado un id

## Pasos 2: Crear el paquete de 'domain'.
Crear objetos para representar los modelos identificados del paso 1 (models, enums,...).
Por ahi vas a ver en ciertas bibliografias que en esta capa agregan servicios, dicho servicios
se centran en la lógica de dominio pura.
Ejemplo: si tuvieras un modelo Producto, dicha clase podria ser ProductService la cual tendria operaciones
que permitan manipular el estado de un producto, por ej: actualizar precio del producto, actualizar el estado del producto, etc. 
(Ojo no confundir con los services/usecases que ellos se encargan de coordinar las logicas para cumplir los casos de usos)
Por otro lado, otras bibliografia directamente no crean estos servicios aparte y embeben estas logicas
dentro de los modelos llevando a tus modelos de estado ANEMIC (simple estructura de datos) a RICH (estructura de datos + logica).

## Pasos 3: Crear el paquete 'application' 
En este paso volcar las reglas de negocio recolectadas en el paso 1 a puertos, es decir a especificaciones (spec) de
tu sistema, es decir en este punto por ahora no pensamos en la implementación solo en especificación mediante interfaces.

### Paso 3.1: Crear puertos primarios (inbound)  (AKA Interfaces inbound)
Es la "spec core" de lo que necesita hacer tu app para cumplir lo anterior. Tambien se los conoce como inbound
o de entrada porque son puertos de entrypoint a la logica central de tu app. Estos puertos realmente no son necesarios
se agregan simplemente para diferenciarlos de los puertos outbound. (Ver al final del readme explico el porque no son utiles ni necesarias las 
interfaces representando los puertos inbound)
```
bff-tienda/application/port/in
```
```java
public interface RegistrarUsuario {
    Usuario execute(Usuario user);
}

public interface ConsultarUsuario {
    Usuario execute(Long userId);
}
```
### Paso 3.2:  Crear puertos secundarios (outbound) (AKA Interfaces outbound)
A diferencia de los puertos principales los puertos secundarios son "spec de soporte" para poder cumplir la casuistica
de la regla de negocio manera completa. Tambien se los conoce como outbound o de salida porque son puertos
de los cuales tu aplicación va a exponer para servicios externos como api's third party o db.
```
bff-tienda/application/port/out
```
Vamos a necesitar 2 puertos uno para guardar, y permitir recuperar usuarios y otro para registrar el alta en el sistema externo entonces:

```java
public interface UsuarioRepositorioPort {
    Usuario guardar(Usuario usuario);
    Usuario retornarPorId(Long userId);
}

public interface UsuarioServicePort {
    void registrar(Usuario usuario);
    void eliminar(Usuario usuario);
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
public class AltaUsuarioUseCase implements RegistrarUsuarioPort {

    private final UsuarioRepositorioPort usuarioRepositorio;

    // Simulamos la necesidad de registrar el alta en un servicio externo ademas de guardarlo en la db
    private final UsuarioServicePort usuarioServicePort;

    @Override
    public Usuario execute(Usuario usuario) {
        usuarioServicePort.registrar(usuario);
        return usuarioRepositorio.guardar(usuario);
    }
}
```

```java
@UseCase
@RequiredArgsConstructor
@Slf4j
public class ObtenerUsuarioUseCase implements ConsultarUsuarioPort {

    private final UsuarioRepositorioPort usuarioRepositorio;

    @Override
    public Usuario execute(Long usuarioId) {
        return usuarioRepositorio.retornarPorId(usuarioId);
    }
}
```

En resumen sobre los usecases:
- Use-cases must be in application module.
- use-cases can be considered as the equivalent of spring services. Both use-cases and spring services have application logic inside. 
- But application and domain modules must be technology independent due to the nature of hexagonal. So, don't use service annotation in use-cases. Make them bean in infrastructure module.

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

    // Tal como menciono en el readme los puertos inbound no son necesarios crearlos por ende aqui podria usar directamente los use-cases
    // Pero esta hecho asi a proposito para dejar bien en claro que la comunicacion de los adaptadores es atraves de los ports de la aplicación.
    private final RegistrarUsuarioPort registrarUsuarioPort;
    private final ConsultarUsuarioPort consultarUsuarioPort;

    private final UsuarioDtoMapper usuarioMapper;

    @GetMapping("users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(usuarioMapper.toDto(consultarUsuarioPort.execute(id)), HttpStatus.OK);

    }

    @PostMapping("users")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto usuarioRequestDto) {
        return new ResponseEntity<>(usuarioMapper.toDto(registrarUsuarioPort.execute(usuarioMapper.toDomain(usuarioRequestDto))),
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

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioServicePort {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioClient usuarioClient;

    @Override
    public void registrar(Usuario usuario) {
        String response = usuarioClient.registrarUsuario();
        log.info("Usuario registrado existosamente en al 3rd Party API, response: {}", response);
    }

    @Override
    public void eliminar(Usuario usuario) {
        String response = usuarioClient.eliminarUsuario();
        log.info("Usuario eliminado existosamente en al 3rd Party API, response: {}", response);
    }
}

@FeignClient(name = "api-usuario", url = "https://httpbin.org")
public interface UsuarioClient {

    @PostMapping(path = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    String registrarUsuario();

    @DeleteMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    String eliminarUsuario();
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

@Configuration
@EnableFeignClients(basePackages = "com.example.bfftienda.infrastructure.adapter.gateway")
public class FeignClientConfig {
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
