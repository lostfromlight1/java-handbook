public class CollectorsOverviewDemo {

    public static void main(String[] args) {
        String vmName = System.getProperty("java.vm.name");
        String vmVendor = System.getProperty("java.vm.vendor");
        System.out.println("vmName=" + vmName);
        System.out.println("vmVendor=" + vmVendor);
        // HotSpot on Java 21 typically uses G1 by default; verify on your JDK with: java -XX:+PrintFlagsFinal -version | findstr UseG1GC
        System.out.println("note=See chapter for verifying default collector flags on your JDK");
    }
}
