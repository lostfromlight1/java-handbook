import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public final class DiagnosticsAwarenessDemo {

    public static void main(String[] args) {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        System.out.println("jvmName=" + runtime.getVmName());
        System.out.println("inputArgsCount=" + runtime.getInputArguments().size());
        System.out.println("threadCount=" + ManagementFactory.getThreadMXBean().getThreadCount());
        System.out.println("note=Use jcmd/jfr for dumps; see Module 31");
    }
}
