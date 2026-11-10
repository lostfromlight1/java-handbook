import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class FfmArenaDemo {

  public static void main(String[] args) {
    try (Arena arena = Arena.ofConfined()) {
      MemorySegment segment = arena.allocate(ValueLayout.JAVA_INT.byteSize() * 2);
      segment.setAtIndex(ValueLayout.JAVA_INT, 0, 21);
      segment.setAtIndex(ValueLayout.JAVA_INT, 1, 42);
      int sum = segment.getAtIndex(ValueLayout.JAVA_INT, 0)
          + segment.getAtIndex(ValueLayout.JAVA_INT, 1);
      System.out.println("sum=" + sum);
      System.out.println("ffmDemo=ok");
    }
  }
}
