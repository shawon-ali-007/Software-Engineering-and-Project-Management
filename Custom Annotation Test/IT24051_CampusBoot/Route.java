import java.lang.annotation.*;

// Element is named value() so the starter code's @Route("/hello") compiles.
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {
    String value();
}
