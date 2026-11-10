public class ClassLoadingDemo {

    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader appLoader = ClassLoadingDemo.class.getClassLoader();
        System.out.println("appLoader=" + (appLoader == null ? "bootstrap" : appLoader.getClass().getName()));

        Class<?> stringClass = Class.forName("java.lang.String");
        System.out.println("stringLoader=" + stringClass.getClassLoader());

        Class<?> demoClass = Class.forName("ClassLoadingDemo");
        System.out.println("demoLoaded=" + (demoClass == ClassLoadingDemo.class));
    }
}
