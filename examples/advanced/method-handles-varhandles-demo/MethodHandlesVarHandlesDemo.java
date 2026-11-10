import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

public class MethodHandlesVarHandlesDemo {

  private volatile int counter;

  private static String greet(String name) {
    return "Hello, " + name;
  }

  public static void main(String[] args) throws Throwable {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodHandle greetHandle =
        lookup.findStatic(
            MethodHandlesVarHandlesDemo.class,
            "greet",
            MethodType.methodType(String.class, String.class));
    String fromHandle = (String) greetHandle.invoke("Ada");
    System.out.println("fromHandle=" + fromHandle);

    MethodHandlesVarHandlesDemo demo = new MethodHandlesVarHandlesDemo();
    VarHandle counterHandle =
        lookup.findVarHandle(
            MethodHandlesVarHandlesDemo.class, "counter", int.class);
    counterHandle.setVolatile(demo, 1);
    int value = (int) counterHandle.getVolatile(demo);
    System.out.println("counter=" + value);
    System.out.println("methodHandlesDemo=ok");
  }
}
