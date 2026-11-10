public class AgentMain {
  public static void premain(String args, java.lang.instrument.Instrumentation inst) {
    System.out.println("agent=premain");
    System.out.println("loadedClasses=" + inst.getAllLoadedClasses().length);
  }
}
