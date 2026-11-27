import java.lang.reflect.Field;
import java.util.*;

public class Container {
    private final Map<String, Object> beans = new LinkedHashMap<>();

    public void scan(Class<?>... classes) {
        for (Class<?> cls : classes) {
            Component comp = cls.getAnnotation(Component.class);
            if (comp == null) {
                System.out.println("[Container] Skipped: " + cls.getSimpleName() + " (no @Component)");
                continue;
            }
            String name = comp.value();
            if (name.isEmpty()) {
                String s = cls.getSimpleName();
                name = Character.toLowerCase(s.charAt(0)) + s.substring(1);
            }
            try {
                Object instance = cls.getDeclaredConstructor().newInstance();
                beans.put(name, instance);
                System.out.println("[Container] Registered: " + name + " (" + cls.getSimpleName() + ")");
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Cannot create bean for " + cls.getName(), e);
            }
        }
    }

    public void injectValues(Map<String, String> config) {
        for (Object bean : beans.values()) {
            Class<?> cls = bean.getClass();
            for (Field field : cls.getDeclaredFields()) {
                Value v = field.getAnnotation(Value.class);
                if (v == null) continue;
                String key = v.value();
                String raw = config.get(key);
                if (raw == null) continue;
                try {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    if (type == int.class || type == Integer.class) {
                        field.set(bean, Integer.parseInt(raw.trim()));
                    } else if (type == String.class) {
                        field.set(bean, raw);
                    } else {
                        continue;
                    }
                    System.out.println("[Container] Injected: " + key + " -> "
                            + cls.getSimpleName() + "." + field.getName());
                } catch (IllegalAccessException | NumberFormatException e) {
                    throw new RuntimeException("Cannot inject " + key + " into " + field.getName(), e);
                }
            }
        }
    }

    public Object getBean(String name) {
        return beans.get(name);
    }

    public Collection<Object> getBeans() {
        return Collections.unmodifiableCollection(beans.values());
    }
}
