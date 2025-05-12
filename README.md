Simula el funcionamiento de una sala de atención al cliente, donde los usuarios solicitan un turno y esperan ser atendidos por una de varias ventanillas disponibles. La aplicación demostrará el uso del broker RabbitMQ como sistema de mensajería para distribuir y gestionar estos turnos entre los consumidores.

Creada por GiselleAndrea


## Estructura
```bash
src/
 └── main/
      └── java/
           └── com/
                └── simulador/
                     └── banco/
                          ├── ClienteProductor.java
                          ├── ReceptorAvisos.java
                          ├── VentanillaConsumidor.java
                          ├── AvisoPublico.java
                          ├── RabbitMQConnection.java
                          └── ui/
                               ├── ClienteProductorUI.java
                               ├── ReceptorAvisosUI.java
                               └── VentanillaConsumidorUI.java
```

## Ejecucion de RabbitMQ

- Comprobar estado
```bash
    sudo systemctl status rabbitmq-server
```
- iniciar Rabbit
```bash
    sudo systemctl start rabbitmq-server
```
- Detener Rabbit
```bash
    sudo systemctl stop rabbitmq-server
```

- Plugin de gestion: 
```bash
    sudo rabbitmq-plugins enable rabbitmq_management
```
-Navegacion
```bash
    http://localhost:15672
    Usuario por defecto: guest
    Contraseña por defecto: guest
```


## Orden de ejecución:
```bash
    mvn clean compile
```
- Avisos generales: src/main/java/com/simulador/banco/ui/ReceptorAvisosUI.java
```bash
    mvn exec:java -Dexec.mainClass="com.simulador.banco.ui.ReceptorAvisosUI"
```
- Ventanillas de atención (Ejecutar varias al tiempo) src/main/java/com/simulador/banco/ui/VentanillaConsumidorUI.java
```bash
    mvn exec:java -Dexec.mainClass="com.simulador.banco.ui.VentanillaConsumidorUI"
```
- Solicitar los turnos src/main/java/com/simulador/banco/ui/ClienteProductorUI.java
```bash
    mvn exec:java -Dexec.mainClass="com.simulador.banco.ui.ClienteProductorUI"
```


