import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
@Retention(RetentionPolicy.RUNTIME)
@interface DemoTag {
    String value();
}

@DemoTag("sample-bean")
class SampleBean {
    public SampleBean() {
    }

    public String greet(String name) {
        return "hello " + name;
    }
}

public class ReflectionAnnotationsProcessDemo {

    public static void main(String[] args) throws Exception {
        Class<?> type = SampleBean.class;
        DemoTag tag = type.getAnnotation(DemoTag.class);
        System.out.println("annotation=" + tag.value());

        Method greet = type.getMethod("greet", String.class);
        Object bean = type.getConstructor().newInstance();
        Object result = greet.invoke(bean, "Ada");
        System.out.println("reflectInvoke=" + result);

        var proc = new ProcessBuilder("java", "-version")
                .redirectErrorStream(true)
                .start();
        String output = new String(proc.getInputStream().readAllBytes());
        boolean hasVersion = output.toLowerCase().contains("version");
        System.out.println("processRan=" + hasVersion);
        System.out.println("processArgs=" + new ProcessBuilder("java", "-version").command());
    }
}
