package kondratovich.offheap.core.allocator;

import kondratovich.offheap.core.ClassMap;
import kondratovich.offheap.core.util.U;
import sun.misc.Unsafe;

public class DirectAllocator extends Allocator {
  private static final Unsafe UNSAFE = U.instance();
  
  DirectAllocator() {}
  
  public Key allocate(long size) {
    return allocate(size, 0, false);
  }
  
  public Key allocate(long size, int len, boolean array) {
    return new Key(UNSAFE.allocateMemory(size), size, len);
  }
  
  public Key allocate(ClassMap cm, long size) {
    return new Key(UNSAFE.allocateMemory(size), size, cm.refs().length);
  }
  
  public void copy(Object o, long offset0, Key ref, long offset1, long size) {
    UNSAFE.copyMemory(o, offset0, null, ref.pointer() + offset1, size);
  }
  
  public void copy(Key o, long offset0, Object ref, long offset1, long size) {
    UNSAFE.copyMemory(null, o.pointer() + offset0, ref, offset1, size);
  }
  
  public void fill(Key ref, long offset0, long size, byte value) {
    UNSAFE.setMemory(ref.pointer() + offset0, size, value);
  }
  
  public long member(Key ref, long offset0) {
    return UNSAFE.getAddress(ref.pointer() + offset0);
  }

  public Object allocate(Class<?> type) throws InstantiationException {
    return UNSAFE.allocateInstance(type);
  }
  
  public void put(Key ref, long offset, int value) {
    UNSAFE.putInt(ref.pointer() + offset, value);
  }

  public void put(Key ref, long offset, double value) {
    UNSAFE.putDouble(ref.pointer() + offset, value);
  }

  public void put(Key ref, long offset, short value) {
    UNSAFE.putShort(ref.pointer() + offset, value);
  }

  public void put(Key ref, long offset, long value) {
    UNSAFE.putLong(ref.pointer() + offset, value);
  }

  public void put(Key ref, long offset, float value) {
    UNSAFE.putFloat(ref.pointer() + offset, value);
  }

  public void put(Key ref, long offset, boolean value) {
    UNSAFE.putByte(ref.pointer() + offset, (byte) (value ? 1 : 0));
  }

  public void put(Key ref, long offset, byte value) {
    UNSAFE.putByte(ref.pointer() + offset, value);
  }

  public void put(Key ref, long offset, char value) {
    UNSAFE.putChar(ref.pointer() + offset, value);
  }

  public int getInt(Key ref, long offset) {
    return UNSAFE.getInt(ref.pointer() + offset);
  }
  
  public double getDouble(Key ref, long offset) {
    return UNSAFE.getDouble(ref.pointer() + offset);
  }

  public short getShort(Key ref, long offset) {
    return UNSAFE.getShort(ref.pointer() + offset);
  }

  public long getLong(Key ref, long offset) {
    return UNSAFE.getLong(ref.pointer() + offset);
  }

  public float getFloat(Key ref, long offset) {
    return UNSAFE.getFloat(ref.pointer() + offset);
  }

  public boolean getBoolean(Key ref, long offset) {
    return UNSAFE.getByte(ref.pointer() + offset) > 0;
  }

  public char getChar(Key ref, long offset) {
    return UNSAFE.getChar(ref.pointer() + offset);
  }

  public byte getByte(Key ref, long offset) {
    return UNSAFE.getByte(ref.pointer() + offset);
  }
}
