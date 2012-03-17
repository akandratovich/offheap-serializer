package org.machariel.core.allocator;

import org.machariel.core.util.Reflection;

public class CheckedDirectAllocator extends DirectAllocator {
  CheckedDirectAllocator() {}
  
  public void copy(Object o, long offset0, Key ref, long offset1, long size) {
    ref.check(offset1, size);
    super.copy(o, offset0, ref, offset1, size);
  }
  
  public void copy(Key ref, long offset0, Object o, long offset1, long size) {
    ref.check(offset0, size);
    super.copy(ref, offset0, o, offset1, size);
  }
  
  public void fill(Key ref, long offset0, long size, byte value) {
    ref.check(offset0, size);
    super.fill(ref, offset0, size, value);
  }
  
  public long member(Key ref, long offset0) {
    ref.check(offset0, Reflection.ADDRESS_SIZE);
    return super.member(ref, offset0);
  }

  public void put(Key ref, long offset, int value) {
    ref.check(offset, Reflection.ARRAY_INT_INDEX_SCALE);
    super.put(ref, offset, value);
  }

  public void put(Key ref, long offset, double value) {
    ref.check(offset, Reflection.ARRAY_DOUBLE_INDEX_SCALE);
    super.put(ref, offset, value);
  }

  public void put(Key ref, long offset, short value) {
    ref.check(offset, Reflection.ARRAY_SHORT_INDEX_SCALE);
    super.put(ref, offset, value);
  }

  public void put(Key ref, long offset, long value) {
    ref.check(offset, Reflection.ARRAY_LONG_INDEX_SCALE);
    super.put(ref, offset, value);
  }

  public void put(Key ref, long offset, float value) {
    ref.check(offset, Reflection.ARRAY_FLOAT_INDEX_SCALE);
    super.put(ref, offset, value);
  }

  public void put(Key ref, long offset, boolean value) {
    ref.check(offset, Reflection.ARRAY_BOOLEAN_INDEX_SCALE);
    super.put(ref, offset, value);
  }

  public void put(Key ref, long offset, byte value) {
    ref.check(offset, Reflection.ARRAY_BYTE_INDEX_SCALE);
    super.put(ref, offset, value);
  }

  public void put(Key ref, long offset, char value) {
    ref.check(offset, Reflection.ARRAY_CHAR_INDEX_SCALE);
    super.put(ref, offset, value);
  }

  public int getInt(Key ref, long offset) {
    ref.check(offset, Reflection.ARRAY_INT_INDEX_SCALE);
    return super.getInt(ref, offset);
  }
  
  public double getDouble(Key ref, long offset) {
    ref.check(offset, Reflection.ARRAY_DOUBLE_INDEX_SCALE);
    return super.getDouble(ref, offset);
  }

  public short getShort(Key ref, long offset) {
    ref.check(offset, Reflection.ARRAY_SHORT_INDEX_SCALE);
    return super.getShort(ref, offset);
  }

  public long getLong(Key ref, long offset) {
    ref.check(offset, Reflection.ARRAY_LONG_INDEX_SCALE);
    return super.getLong(ref, offset);
  }

  public float getFloat(Key ref, long offset) {
    ref.check(offset, Reflection.ARRAY_FLOAT_INDEX_SCALE);
    return super.getFloat(ref, offset);
  }

  public boolean getBoolean(Key ref, long offset) {
    ref.check(offset, Reflection.ARRAY_BOOLEAN_INDEX_SCALE);
    return super.getBoolean(ref, offset);
  }

  public char getChar(Key ref, long offset) {
    ref.check(offset, Reflection.ARRAY_CHAR_INDEX_SCALE);
    return super.getChar(ref, offset);
  }

  public byte getByte(Key ref, long offset) {
    ref.check(offset, Reflection.ARRAY_BYTE_INDEX_SCALE);
    return super.getByte(ref, offset);
  }
}
