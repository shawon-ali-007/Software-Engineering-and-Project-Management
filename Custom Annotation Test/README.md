# CampusBoot

A tiny annotation-driven mini-framework built with **plain Java 11+ and Java Reflection** (no external libraries). It demonstrates how frameworks like Spring Boot work under the hood: annotations + reflection, not magic.

## Features

| Feature | Annotation | Implemented by |
|---|---|---|
| Bean creation (IoC container) | `@Component` | `Container.scan` |
| Configuration injection | `@Value` | `Container.injectValues` |
| Field validation | `@Range` | `Validator.validate` |
| URL-to-method routing | `@Route` | `Router.dispatch` |

## Project Structure

```
CampusBoot/
├── Component.java        # @Component  (TYPE, RUNTIME, value() default "")
├── Value.java            # @Value      (FIELD, RUNTIME, value())
├── Range.java            # @Range      (FIELD, RUNTIME, min, max, message default "Value out of range")
├── Route.java            # @Route      (METHOD, RUNTIME, value())
├── Container.java        # IoC container + value injector
├── Validator.java        # Reflection-based validator
├── Router.java           # Path dispatcher
├── GradeController.java  # Sample bean (starter code)
├── ReportPrinter.java    # Un-annotated class, skipped by the container
├── Student.java          # Sample validated model
├── Main.java             # Demo entry point
├── answers.txt           # Reflection questions
└── README.md
```

## Requirements

- JDK 11 or newer

## Build & Run

```bash
javac *.java
java Main
```

## Expected Output

```
[Container] Registered: gradeController (GradeController)
[Container] Skipped: ReportPrinter (no @Component)
[Container] Injected: app.name -> GradeController.appName
[Container] Injected: app.passMark -> GradeController.passMark
--- Validation ---
VIOLATION: score - Score must be between 0 and 100 (actual: 120)
OK
VIOLATION: semester - Value out of range (actual: 15)
--- Routing ---
[Router] /hello -> Welcome to CampusBoot
[Router] /status -> Pass mark is 40
[Router] /unknown -> 404 Not Found: /unknown
```

## How It Works

### Annotations
All four annotations use `@Retention(RetentionPolicy.RUNTIME)` so they remain visible to reflection at runtime, and a `@Target` that restricts where they can be placed (class, field, or method).

### Container
- `scan(Class<?>...)` reads `@Component` on each class, instantiates it through its no-arg constructor, and stores it in a `Map<String,Object>`. The bean name is `value()`, or the simple class name with a lowercase first letter if empty. Classes without `@Component` are skipped.
- `injectValues(Map<String,String>)` finds `@Value` fields on every bean, calls `setAccessible(true)`, converts the string to `int` when needed, and sets the field. Missing keys and unsupported types are skipped.
- `getBean(String)` returns a bean by name.

### Validator
`Validator.validate(Object)` inspects every `@Range` `int` field and returns violations formatted as:

```
fieldName - message (actual: value)
```

An empty list means the object is valid.

### Router
`Router.dispatch(Container, String)` searches all beans for a method whose `@Route` value equals the requested path, invokes it, and returns the result. If nothing matches it returns `404 Not Found: <path>`.

## Design Notes

- **`@Route` element name:** the specification lists `String path()`, but the provided starter code uses the shorthand `@Route("/hello")`, which requires the element to be named `value`. `value()` is used so the starter code compiles unmodified.
- **Bean ordering:** `LinkedHashMap` preserves registration order for predictable output.
- **Router access:** `Container.getBeans()` exposes an unmodifiable view of beans for the router.

## CampusBoot vs. Spring Boot

| CampusBoot | Spring Boot equivalent |
|---|---|
| `@Component` | `@Component`, `@Service`, `@Repository` |
| `@Value` | `@Value` |
| `@Range` | Bean Validation `@Min` / `@Max` |
| `@Route` | `@GetMapping` / `@RequestMapping` |

Not implemented here: dependency injection between beans (`@Autowired`), classpath scanning, bean lifecycle management, and AOP proxies (e.g. `@Transactional`).

## License

Created for educational purposes as a Java Reflection & Custom Annotations assignment.
