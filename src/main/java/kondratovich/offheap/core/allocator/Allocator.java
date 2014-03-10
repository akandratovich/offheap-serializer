package kondratovich.offheap.core.allocator;

import kondratovich.offheap.core.util.U;
import sun.misc.Unsafe;

public abstract class Allocator implements IAllocator {
  private static final Unsafe UNSAFE = U.instance();
  public static final Allocator DIRECT = new DirectAllocator();
  public static final Allocator CHECKED = new CheckedDirectAllocator();
  
  public static Object getObject(Object o, long offset) {
    return UNSAFE.getObject(o, offset);
  }
  
  public static void put(Object o, long offset, int value) {
    UNSAFE.putInt(o, offset, value);
  }

  public static void put(Object o, long offset, double value) {
    UNSAFE.putDouble(o, offset, value);
  }

  public static void put(Object o, long offset, long value) {
    UNSAFE.putLong(o, offset, value);
  }

  public static void put(Object o, long offset, float value) {
    UNSAFE.putFloat(o, offset, value);
  }

  public static void put(Object o, long offset, char value) {
    UNSAFE.putChar(o, offset, value);
  }

  public static void put(Object o, long offset, boolean value) {
    UNSAFE.putBoolean(o, offset, value);
  }

  public static void put(Object o, long offset, byte value) {
    UNSAFE.putByte(o, offset, value);
  }

  public static void put(Object o, long offset, short value) {
    UNSAFE.putShort(o, offset, value);
  }
  
  public static void put(Object o, long offset, Object value) {
    UNSAFE.putObject(o, offset, value);
  }
}
