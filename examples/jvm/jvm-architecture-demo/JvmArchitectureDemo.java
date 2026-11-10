import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class JvmArchitectureDemo {

    public static void main(String[] args) {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        System.out.println("jvmName=" + runtime.getVmName());
        System.out.println("javaVersion=" + runtime.getSpecVersion());
        System.out.println("pid=" + ProcessHandle.current().pid());
        System.out.println("inputArgsCount=" + runtime.getInputArguments().size());
    }
}
