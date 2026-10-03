# Waifu Nexus OO

Proyecto académico en Java 17 para practicar Análisis y Diseño Orientado a Objetos
con un dominio original de colección y combate de personajes anime ficticios.

## Contenidos cubiertos

El cronograma de la materia incluye modelo de dominio, casos de uso/secuencia,
GRASP, SOLID, MVC, Strategy, Adapter, Composite, Decorator, Facade, State,
Observer, Singleton, Factory, DAO, Repository y Table Data Gateway. Este proyecto
los integra en una sola aplicación de consola.

### Patrones

- **Factory:** `WaifuFactory`
- **Singleton:** `DatabaseConnection`
- **Strategy:** `CombatStrategy` + estrategias concretas
- **Adapter:** `AnimeApiAdapter`
- **Composite:** `Squad` + `SquadComponent`
- **Decorator:** `PowerUpDecorator`, `ArmorDecorator`
- **Facade:** `AnimeGameFacade`
- **State:** `HealthyState`, `InjuredState`, `KnockedOutState`
- **Observer:** `Subject`, `Observer`, `AchievementObserver`, `ConsoleNotificationObserver`
- **MVC:** `WaifuController`, `ConsoleView`, entidades/modelo de dominio
- **DAO:** `WaifuDAO`, `InMemoryWaifuDAO`
- **Repository:** `WaifuRepository`, `WaifuRepositoryImpl`
- **Table Data Gateway:** `WaifuTableDataGateway`

## GRASP aplicado

- **Controller:** `WaifuController`
- **Creator:** `WaifuFactory`
- **Information Expert:** `Waifu` calcula su daño y administra su estado
- **Low Coupling:** interfaces para DAO, Repository, Strategy y Observer
- **High Cohesion:** responsabilidades separadas por paquetes
- **Polymorphism:** estados y estrategias intercambiables
- **Pure Fabrication:** DAO/Repository para persistencia
- **Indirection:** Adapter y Facade desacoplan subsistemas

## Ejecutar

Requiere Java 17+ y Maven.

```bash
mvn clean compile
mvn exec:java
```

También se puede compilar sin Maven:

```bash
javac -d out $(find src/main/java -name "*.java")
java -cp out com.waifu.Main
```

## Funcionalidades de la demo

1. Crea waifus mediante Factory.
2. Persiste y consulta mediante DAO + Repository + Table Data Gateway.
3. Importa un personaje de una API externa simulada mediante Adapter.
4. Forma un escuadrón con Composite.
5. Agrega buffs mediante Decorator.
6. Ejecuta combates con Strategy.
7. Cambia estados mediante State.
8. Notifica eventos mediante Observer.
9. Orquesta todo desde Facade y expone casos de uso desde Controller/MVC.

## 🖥️ Interfaz gráfica — Waifu Nexus Battle Arena

La aplicación ahora incluye una interfaz gráfica en **Java Swing** para reemplazar la demo de consola.

Desde la ventana principal se puede:

- Seleccionar la waifu atacante.
- Seleccionar la waifu defensora.
- Cambiar la estrategia de combate: Agresiva, Equilibrada o Defensiva.
- Visualizar imagen, anime, elemento, ATK, DEF, HP y estado.
- Ver barras de vida actualizadas después de cada acción.
- Ejecutar ataques y observar el daño producido.
- Curar a la waifu seleccionada.
- Reiniciar una batalla.
- Consultar un registro visual de combate.
- Recibir eventos del patrón Observer directamente en la interfaz.
- Visualizar un logro cuando se alcanza el evento correspondiente.

Las imágenes se encuentran en:

```text
src/main/resources/images/
```

La interfaz mantiene la separación de responsabilidades: el Swing UI no calcula el daño, sino que delega el caso de uso a `AnimeGameFacade`, que utiliza `Strategy`, `State` y `Observer`.

## ⚔️ Sistema de combate por equipos

La versión gráfica utiliza dos equipos de combate:

- **Equipo del jugador:** Akari y Mizuki.
- **Equipo enemigo:** Yoru y Luna.

Cada ronda funciona de la siguiente manera:

1. El jugador selecciona una de sus waifus vivas.
2. Selecciona una waifu enemiga viva como objetivo.
3. Elige la estrategia de combate.
4. Ejecuta el ataque.
5. Si la enemiga llega a 0 HP, queda en estado **KO / ELIMINADA** y desaparece de los selectores de combate.
6. Si todavía quedan enemigas, una enemiga viva realiza automáticamente su contraataque.
7. Las enemigas se van alternando para que todas las que sigan vivas puedan atacar.
8. Si una waifu del jugador llega a 0 HP, también queda eliminada y ya no puede volver a seleccionarse durante esa batalla.
9. La batalla termina cuando uno de los dos equipos queda completamente eliminado.
10. **Nueva Batalla** restaura el HP y vuelve a habilitar a todas las waifus.

Esto permite representar un combate real entre dos equipos y evita que un personaje KO pueda continuar participando.

## 🎮 Nueva versión gráfica y sistema de combate

La versión actual utiliza una interfaz gráfica Swing con flujo de videojuego:

1. **Pantalla principal** con `Nuevo Combate` y `Continuar Combate Anterior`.
2. **Selección de equipos:** se eligen entre 1 y 3 waifus aliadas y entre 1 y 3 enemigas.
3. **Combate por turnos:** las enemigas también atacan automáticamente.
4. **Eliminación:** al llegar a 0 HP una waifu desaparece del campo, se elimina de los selectores y no puede volver a participar en esa batalla.
5. **Guardado automático:** el combate guarda composición, HP, turno, selección, estrategia, registro y daño acumulado en el archivo `.waifu-nexus-battle.properties` dentro del directorio personal del usuario.
6. **Continuar:** reconstruye el combate anterior desde el estado guardado.
7. **Resultado:** al terminar se muestra una pantalla de victoria o derrota con las waifus correspondientes y su puntaje final.
8. **Volver a jugar:** elimina el flujo anterior y vuelve al menú principal para comenzar otra partida.

El proyecto contiene seis personajes jugables para permitir formaciones de hasta 3 contra 3:

- Akari — FIRE
- Mizuki — WATER
- Yoru — SHADOW
- Luna — LIGHT
- Fuyumi — WIND
- Hikari — LIGHT

Las imágenes que no tienen un recurso específico todavía utilizan el placeholder integrado de `ImageRepository`, por lo que el juego sigue siendo ejecutable sin depender de servicios externos.

# 🌸 Waifu Nexus — Object-Oriented Design Lab

> **Proyecto académico de Análisis y Diseño Orientado a Objetos desarrollado en Java.**

**Waifu Nexus** es una aplicación de consola ambientada en un universo de personajes anime ficticios, creada para demostrar de forma práctica diferentes conceptos de **Programación Orientada a Objetos, Diseño Orientado a Objetos y Patrones de Diseño**.

El proyecto simula un pequeño sistema donde se pueden crear personajes, almacenarlos, formar escuadrones, importar personajes externos, aplicar mejoras y realizar combates utilizando diferentes estrategias.

El objetivo principal no es solamente desarrollar una aplicación funcional, sino demostrar **cómo aplicar correctamente principios de diseño orientado a objetos y patrones de diseño en un proyecto real**.

---

## 🎯 Objetivos del proyecto

El proyecto fue diseñado para poner en práctica los siguientes conceptos:

* Programación Orientada a Objetos
* Modelado de dominio
* Responsabilidad única
* Bajo acoplamiento
* Alta cohesión
* Polimorfismo
* Encapsulamiento
* Interfaces
* Herencia
* Composición
* GRASP
* SOLID
* MVC
* Patrones creacionales
* Patrones estructurales
* Patrones de comportamiento
* Persistencia y acceso a datos
* Separación de responsabilidades

---

# 🎮 Concepto del sistema

En **Waifu Nexus**, el usuario administra un conjunto de personajes anime ficticios conocidos como *Waifus*.

Cada personaje posee características como:

* Nombre
* Anime/origen
* Elemento
* Ataque
* Defensa
* Puntos de vida
* Estado actual

Los personajes pueden:

* Ser creados mediante una Factory.
* Guardarse mediante Repository y DAO.
* Ser importados desde una fuente externa mediante Adapter.
* Integrarse en escuadrones mediante Composite.
* Recibir mejoras mediante Decorator.
* Combatir utilizando distintas estrategias.
* Cambiar de estado durante el combate.
* Generar eventos mediante Observer.

Todo el sistema se encuentra coordinado mediante una **Facade**, mientras que el acceso desde la aplicación se organiza utilizando una estructura **MVC**.

---

# 🏗️ Arquitectura

El proyecto está dividido en diferentes paquetes según responsabilidades:

```text
src/main/java/com/waifu/

├── Main.java
│
├── model/
│   ├── Waifu.java
│   ├── Element.java
│   ├── WaifuState.java
│   ├── HealthyState.java
│   ├── InjuredState.java
│   ├── KnockedOutState.java
│   ├── SquadComponent.java
│   ├── SquadMember.java
│   ├── Squad.java
│   ├── WaifuPower.java
│   ├── BaseWaifuPower.java
│   ├── WaifuDecorator.java
│   ├── PowerUpDecorator.java
│   └── ArmorDecorator.java
│
├── factory/
│   └── WaifuFactory.java
│
├── strategy/
│   ├── CombatStrategy.java
│   ├── AggressiveStrategy.java
│   ├── BalancedStrategy.java
│   └── DefensiveStrategy.java
│
├── adapter/
│   └── AnimeApiAdapter.java
│
├── external/
│   ├── ExternalAnimeApi.java
│   ├── ExternalCharacter.java
│   └── FakeAnimeApi.java
│
├── observer/
│   ├── Observer.java
│   ├── Subject.java
│   ├── ConsoleNotificationObserver.java
│   └── AchievementObserver.java
│
├── persistence/
│   ├── DatabaseConnection.java
│   ├── WaifuDAO.java
│   ├── InMemoryWaifuDAO.java
│   ├── WaifuRepository.java
│   ├── WaifuRepositoryImpl.java
│   └── WaifuTableDataGateway.java
│
├── facade/
│   └── AnimeGameFacade.java
│
├── controller/
│   └── WaifuController.java
│
└── view/
    └── ConsoleView.java
```

---

# 🧩 Patrones de Diseño

El proyecto implementa varios patrones de diseño para resolver diferentes problemas de arquitectura.

## 🏭 Factory

### `WaifuFactory`

La Factory se encarga de crear diferentes tipos de personajes sin que el resto del sistema tenga que conocer los detalles de construcción de cada uno.

```java
Waifu akari = factory.create("fire");
Waifu mizuki = factory.create("water");
Waifu yoru = factory.create("shadow");
```

Esto centraliza la creación de objetos y evita tener lógica de construcción repetida por todo el sistema.

---

# ♻️ Singleton

### `DatabaseConnection`

La conexión a la base de datos simulada utiliza Singleton.

```java
DatabaseConnection connection =
        DatabaseConnection.getInstance();
```

El objetivo es garantizar que exista una única instancia compartida de la conexión.

En este proyecto la persistencia es simulada mediante estructuras en memoria, pero el patrón representa cómo podría centralizarse una conexión real.

---

# ⚔️ Strategy

### `CombatStrategy`

El sistema de combate permite intercambiar dinámicamente la estrategia utilizada para calcular el daño.

Se implementaron:

```text
CombatStrategy
├── AggressiveStrategy
├── BalancedStrategy
└── DefensiveStrategy
```

Por ejemplo:

```java
facade.fight(
    akari,
    mizuki,
    new AggressiveStrategy()
);
```

Cada estrategia posee un algoritmo diferente para calcular el daño.

Esto permite agregar nuevas estrategias sin modificar la clase principal `Waifu`.

---

# 🔌 Adapter

### `AnimeApiAdapter`

El sistema simula una API externa de anime.

La estructura externa utiliza sus propios objetos:

```text
ExternalAnimeApi
ExternalCharacter
```

Mientras que el sistema interno utiliza:

```text
Waifu
```

El Adapter funciona como puente entre ambos modelos.

```text
API externa
     ↓
AnimeApiAdapter
     ↓
Waifu
```

De esta manera, el dominio interno no depende directamente de la estructura de la API externa.

---

# 🌳 Composite

### `SquadComponent`

El patrón Composite permite representar una estructura jerárquica de personajes.

```text
Squad
 ├── SquadMember → Akari
 ├── SquadMember → Mizuki
 └── SquadMember → Luna
```

Tanto los elementos individuales como el escuadrón completo implementan:

```java
SquadComponent
```

Por lo tanto, podemos consultar:

```java
squad.totalAttack();
squad.totalDefense();
squad.totalHp();
```

sin tener que preocuparnos por si estamos trabajando con una waifu individual o con un grupo.

---

# ✨ Decorator

### `WaifuDecorator`

Permite agregar mejoras dinámicamente sin modificar la clase original.

Por ejemplo:

```java
WaifuPower build =
    new ArmorDecorator(
        new PowerUpDecorator(
            new BaseWaifuPower(yoru)
        )
    );
```

La estructura queda:

```text
Yoru
 ↓
PowerUpDecorator
 ↓
ArmorDecorator
```

En este caso se agregan:

* Núcleo Arcano → aumenta ataque.
* Armadura Lunar → aumenta defensa.

El beneficio principal es poder combinar mejoras sin crear una clase diferente para cada combinación posible.

---

# 🔄 State

### `WaifuState`

Una waifu puede encontrarse en diferentes estados durante una batalla:

```text
WaifuState
├── HealthyState
├── InjuredState
└── KnockedOutState
```

El comportamiento cambia según el estado.

Por ejemplo:

```text
SALUDABLE
    ↓ recibe daño
HERIDA
    ↓ recibe suficiente daño
KO
```

Cuando una waifu está en `KnockedOutState`, ya no puede atacar.

Esto evita tener grandes bloques de `if/else` dentro de `Waifu`.

---

# 👁️ Observer

### `Observer` / `Subject`

Los personajes pueden notificar eventos a diferentes observadores.

Por ejemplo:

```text
Waifu
  │
  ├── ConsoleNotificationObserver
  │
  └── AchievementObserver
```

Cuando ocurre un evento:

```java
notifyObservers(event);
```

los observadores reciben la información.

Esto permite desacoplar la generación del evento de las acciones que deben realizarse como consecuencia.

---

# 🎭 Facade

### `AnimeGameFacade`

La Facade proporciona una interfaz sencilla para utilizar diferentes subsistemas.

Internamente coordina:

```text
Factory
Repository
Adapter
Strategy
Composite
Decorator
Observer
State
```

Desde el Controller podemos hacer:

```java
facade.createAndSave("fire");
facade.importAndSave("Luna");
facade.fight(akari, mizuki, strategy);
```

sin tener que conocer todos los detalles internos.

---

# 🗄️ Persistencia

El proyecto implementa diferentes patrones relacionados con el acceso a datos.

## DAO

### `WaifuDAO`

Define las operaciones de acceso a los datos:

```java
save()
findById()
findAll()
```

La implementación utilizada es:

```text
InMemoryWaifuDAO
```

---

## Repository

### `WaifuRepository`

El Repository proporciona una abstracción de más alto nivel para trabajar con las entidades del dominio.

```text
WaifuRepository
       ↓
WaifuRepositoryImpl
       ↓
WaifuDAO
       ↓
WaifuTableDataGateway
```

Esto mantiene al dominio desacoplado de los detalles de persistencia.

---

## Table Data Gateway

### `WaifuTableDataGateway`

Esta clase representa el acceso directo a la estructura que almacena los datos.

Actualmente se utiliza un:

```java
Map<Integer, Waifu>
```

para simular una tabla de datos.

---

# 🖥️ MVC

El proyecto también utiliza una separación basada en **Model - View - Controller**.

```text
              ┌─────────────┐
              │ Controller  │
              └──────┬──────┘
                     │
                     ▼
              ┌─────────────┐
              │   Facade    │
              └──────┬──────┘
                     │
                     ▼
              ┌─────────────┐
              │    Model    │
              └─────────────┘

              ┌─────────────┐
              │    View     │
              └─────────────┘
```

### Model

Está compuesto principalmente por las entidades del dominio:

```text
Waifu
Squad
WaifuState
WaifuPower
etc.
```

### View

La salida de consola está encapsulada en:

```text
ConsoleView
```

### Controller

La coordinación de los casos de uso de la aplicación está en:

```text
WaifuController
```

---

# 🧠 GRASP

El diseño también aplica diferentes principios GRASP.

### Controller

`WaifuController` recibe y coordina las operaciones provenientes de la aplicación.

### Creator

`WaifuFactory` se encarga de crear objetos `Waifu`.

### Information Expert

`Waifu` posee la información necesaria para realizar operaciones propias del personaje, como administrar vida, estado y daño base.

### Polymorphism

El polimorfismo aparece principalmente en:

```text
WaifuState
CombatStrategy
Observer
SquadComponent
WaifuPower
```

### Low Coupling

Se utilizan interfaces para evitar dependencias innecesarias entre componentes.

### High Cohesion

Cada paquete y clase intenta concentrarse en una responsabilidad concreta.

### Pure Fabrication

Las clases de infraestructura como:

```text
DAO
Repository
TableDataGateway
```

separan responsabilidades técnicas del dominio.

### Indirection

`Facade`, `Adapter`, `Repository` y otras abstracciones funcionan como intermediarios para reducir el acoplamiento.

---

# 🧱 Principios SOLID

## S — Single Responsibility Principle

Las responsabilidades están separadas.

Por ejemplo:

```text
Waifu
    → comportamiento del personaje

WaifuFactory
    → creación

WaifuDAO
    → acceso a datos

ConsoleView
    → presentación
```

---

## O — Open/Closed Principle

El sistema permite agregar nuevas estrategias, estados, decoradores u observadores sin tener que modificar las clases existentes.

Por ejemplo:

```text
CombatStrategy
├── AggressiveStrategy
├── BalancedStrategy
├── DefensiveStrategy
└── NuevaEstrategia
```

---

## L — Liskov Substitution Principle

Las implementaciones concretas pueden utilizarse mediante sus abstracciones.

Por ejemplo:

```java
CombatStrategy strategy =
    new AggressiveStrategy();
```

---

## I — Interface Segregation Principle

Las interfaces son pequeñas y específicas.

Ejemplos:

```text
Observer
Subject
CombatStrategy
WaifuDAO
WaifuRepository
SquadComponent
```

---

## D — Dependency Inversion Principle

Las capas superiores dependen de abstracciones y no directamente de implementaciones concretas.

Por ejemplo:

```text
WaifuRepository
       ↑
WaifuRepositoryImpl
```

y:

```text
CombatStrategy
       ↑
AggressiveStrategy
BalancedStrategy
DefensiveStrategy
```

---

# 🔄 Flujo principal de la aplicación

Un ejemplo del flujo de creación de una waifu es:

```text
Usuario
   │
   ▼
WaifuController
   │
   ▼
AnimeGameFacade
   │
   ▼
WaifuFactory
   │
   ▼
Waifu
   │
   ▼
WaifuRepository
   │
   ▼
WaifuDAO
   │
   ▼
WaifuTableDataGateway
```

---

# ⚔️ Flujo de combate

Durante un combate:

```text
WaifuController
       │
       ▼
AnimeGameFacade
       │
       ▼
CombatStrategy
       │
       ▼
Waifu.calculateBaseDamage()
       │
       ▼
Waifu.receiveDamage()
       │
       ▼
WaifuState
       │
       ├── HealthyState
       ├── InjuredState
       └── KnockedOutState
       │
       ▼
Observer
       │
       ├── ConsoleNotificationObserver
       └── AchievementObserver
```

Esto permite que el combate involucre varios patrones sin que una única clase concentre toda la lógica.

---

# 🌸 Personajes incluidos

El proyecto contiene personajes ficticios creados específicamente para la aplicación.

| Personaje | Elemento  | ATK | DEF |  HP |
| --------- | --------- | --: | --: | --: |
| Akari     | 🔥 FIRE   |  80 |  35 | 180 |
| Mizuki    | 💧 WATER  |  65 |  55 | 200 |
| Yoru      | 🌑 SHADOW |  90 |  30 | 170 |
| Luna      | ✨ LIGHT   |  76 |  50 | 195 |

Estos personajes no representan personajes oficiales de ninguna franquicia.

---

# 🚀 Ejecución

## Requisitos

* Java 17 o superior
* Maven 3.x

## Con Maven

```bash
mvn clean compile
mvn exec:java
```

## Sin Maven

También se puede compilar directamente:

```bash
javac -d out $(find src/main/java -name "*.java")
```

Y ejecutar:

```bash
java -cp out com.waifu.Main
```

---

# 📊 Ejemplo de salida

```text
==============================================
        WAIFU NEXUS - OO DESIGN LAB
==============================================

--- MVC + FACTORY + ADAPTER + REPOSITORY ---

Akari [Crimson Academy] - FIRE
ATK 80 DEF 35 HP 180/180 | SALUDABLE

Mizuki [Azure Moon] - WATER
ATK 65 DEF 55 HP 200/200 | SALUDABLE

Yoru [Nightfall Academy] - SHADOW
ATK 90 DEF 30 HP 170/170 | SALUDABLE

--- COMPOSITE ---

+ Team Eclipse [ATK=221, DEF=140, HP=575]
  - Akari
  - Mizuki
  - Luna

--- DECORATOR ---

Yoru + Núcleo Arcano + Armadura Lunar
ATK=115 DEF=50 HP=170

--- STRATEGY + STATE + OBSERVER ---

=== DUELO: Akari vs Mizuki ===
Estrategia: AGRESIVA

[OBSERVER] Mizuki recibió daño.

[OBSERVER] Mizuki cambió a estado HERIDA.

[ACHIEVEMENT] ¡Primer vínculo de batalla desbloqueado!

[OBSERVER] Mizuki cambió a estado KO.
```

---

# 📐 Diagrama UML

El proyecto cuenta con un diagrama de clases UML que representa las principales clases, interfaces, relaciones, herencias y patrones utilizados.

La arquitectura puede resumirse conceptualmente como:

```text
                    WAIFU NEXUS
                         │
       ┌─────────────────┼─────────────────┐
       │                 │                 │
     MODEL           BUSINESS          INFRASTRUCTURE
       │                 │                 │
       │            ┌────┴────┐       ┌────┴─────┐
       │            │         │       │          │
    Waifu        Strategy   Factory  DAO     Repository
       │
   ┌───┼────┐
   │   │    │
 State Squad Power
   │   │    │
   │   │ Decorator
   │   │
Observer
```

---

# 📚 Relación con la materia

El proyecto fue pensado como una integración práctica de los temas trabajados durante la cursada de **Análisis y Diseño Orientado a Objetos - Proceso de Desarrollo de Software**.

Los contenidos del cronograma incluyen:

* Introducción a ADOO y repaso de POO.
* Modelo de dominio.
* Casos de uso.
* Diagramas de secuencia.
* GRASP.
* SOLID.
* Code smells.
* MVC.
* Strategy.
* Adapter.
* Transformación de modelo a código.
* Patrones de diseño.
* Composite.
* Decorator.
* Facade.
* State.
* Observer.
* Singleton.
* Factory.
* DAO.
* Repository.
* Table Data Gateway.

**Waifu Nexus** integra estos conceptos dentro de un único dominio para que puedan observarse en conjunto y no como ejemplos aislados.

---

# 🛠️ Tecnologías

* **Java 17**
* **Maven**
* Programación Orientada a Objetos
* UML
* Patrones de Diseño
* Arquitectura MVC
* Persistencia simulada en memoria

---

# 👨‍💻 Propósito académico

Este proyecto fue desarrollado con fines educativos para demostrar la aplicación práctica de principios y patrones de diseño orientado a objetos.

La idea central es mostrar que los patrones no son simplemente clases adicionales, sino herramientas para **organizar responsabilidades, reducir acoplamiento, mejorar extensibilidad y facilitar el mantenimiento del software**.

---

# 🌸 Waifu Nexus

> **More Waifus, More Power.**

Un pequeño laboratorio de diseño orientado a objetos disfrazado de juego de anime. ✨

<img width="1312" height="1199" alt="Diagrama UML de Waifu Nexus Java" src="https://github.com/user-attachments/assets/b488967f-c1a2-4bbb-8aac-f9a8522d646a" />

<img width="1536" height="1024" alt="Interfaz de batalla anime en Waifu Nexus" src="https://github.com/user-attachments/assets/d5e9de61-8d9d-458a-bddf-3437f0b23d06" />

<img width="1536" height="1024" alt="Montaje del flujo de Waifu Nexus" src="https://github.com/user-attachments/assets/5c1d0f4c-35b7-46de-8377-4da7693052d7" />

https://github.com/user-attachments/assets/f94af334-8a8d-478a-afe4-1a9ca86b9331
