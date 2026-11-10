import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@interface Timed {
}

public class ReflectionAnnotationsDemo {

    @Timed
    public static String greet(String name) {
        return "Hello, " + name;
    }

    public static void main(String[] args) throws ReflectiveOperationException {
        Method m = ReflectionAnnotationsDemo.class.getMethod("greet", String.class);
        boolean hasTimed = m.isAnnotationPresent(Timed.class);
        Object result = m.invoke(null, "Lin");
        System.out.println("hasTimed=" + hasTimed);
        System.out.println("result=" + result);
        System.out.println("reflectionDemo=ok");
    }
}
