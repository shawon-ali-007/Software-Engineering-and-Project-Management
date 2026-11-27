import java.lang.reflect.Field;
import java.util.*;

public class Validator {
    public static List<String> validate(Object obj) {
        List<String> violations = new ArrayList<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            Range r = field.getAnnotation(Range.class);
            if (r == null) continue;
            if (field.getType() != int.class) continue;
            try {
                field.setAccessible(true);
                int actual = field.getInt(obj);
                if (actual < r.min() || actual > r.max()) {
                    violations.add(field.getName() + " - " + r.message() + " (actual: " + actual + ")");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return violations;
    }
}
