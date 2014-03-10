package kondratovich.offheap.core.access;

import kondratovich.offheap.core.allocator.Allocator;
import kondratovich.offheap.core.allocator.Key;
import kondratovich.offheap.core.serialization.Serializer;
import kondratovich.offheap.core.util.Reflection;


public class ArrayAccessor<A> {
	private final ObjectAccessor<A> sm;
	private final Serializer serializer;
	
	public ObjectAccessor<A> getMemberAccessor() {
	  return sm;
	}
	
  public ArrayAccessor(Class<A[]> array) {
    this(array, true);
  }
  
  @SuppressWarnings("unchecked")
  public ArrayAccessor(Class<A[]> array, boolean checked) {
    Class<A> cl = (Class<A>) array.getComponentType();
    if (cl.isPrimitive()) throw new IllegalArgumentException();
    
		sm = new ObjectAccessor<A>((Class<A>) array.getComponentType(), checked);
		serializer = checked ? Serializer.CHECKED : Serializer.DIRECT;
	}
	
  public Key getMember(Key ref, int index) {
    return ref.member(index);
  }
  
  public void putReference(Key ref, int index, Key value) {
    ref.member(index, value);
  }
  
  @SuppressWarnings("unchecked")
  public A get(Key ref, int index) throws InstantiationException {
    return (A) serializer.deserialize(ref.member(index));
  }
  
  public void put(Key ref, int index, A value) {
    put(ref, index, value, 0);
  }
  
  public void put(Key ref, int index, A value, int deep) {
    ref.member(index, serializer.serialize(value, deep));
  }
  
	public static class Raw {
	  public static final Raw CHECKED = new Raw(true);
	  public static final Raw DIRECT = new Raw(false);
	  
	  private final Allocator allocator;
	  
	  private Raw(boolean checked) {
	    allocator = checked ? Allocator.CHECKED : Allocator.DIRECT;
    }
	  
    public void put(Key ref, int index, int value) {
      allocator.put(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_INT_INDEX_SCALE, value);
    }
    
    public void put(Key ref, int index, short value) {
      allocator.put(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_SHORT_INDEX_SCALE, value);
    }
    
    public void put(Key ref, int index, long value) {
      allocator.put(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_LONG_INDEX_SCALE, value);
    }
    
    public void put(Key ref, int index, char value) {
      allocator.put(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_CHAR_INDEX_SCALE, value);
    }
    
    public void put(Key ref, int index, byte value) {
      allocator.put(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_BYTE_INDEX_SCALE, value);
    }
    
    public void put(Key ref, int index, boolean value) {
      allocator.put(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_BOOLEAN_INDEX_SCALE, (byte) (value ? 1 : 0));
    }
    
    public void put(Key ref, int index, double value) {
      allocator.put(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_DOUBLE_INDEX_SCALE, value);
    }
    
    public void put(Key ref, int index, float value) {
      allocator.put(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_FLOAT_INDEX_SCALE, value);
    }
    
    public int getInt(Key ref, int index) {
      return allocator.getInt(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_INT_INDEX_SCALE);
    }
    
    public boolean getBoolean(Key ref, int index) {
      return allocator.getByte(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_BOOLEAN_INDEX_SCALE) > 0;
    }
    
    public byte getByte(Key ref, int index) {
      return allocator.getByte(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_BYTE_INDEX_SCALE);
    }
    
    public char getChar(Key ref, int index) {
      return allocator.getChar(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_CHAR_INDEX_SCALE);
    }
    
    public double getDouble(Key ref, int index) {
      return allocator.getDouble(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_DOUBLE_INDEX_SCALE);
    }
    
    public float getFloat(Key ref, int index) {
      return allocator.getFloat(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_FLOAT_INDEX_SCALE);
    }
    
    public short getShort(Key ref, int index) {
      return allocator.getShort(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_SHORT_INDEX_SCALE);
    }
    
    public long getLong(Key ref, int index) {
      return allocator.getLong(ref, Serializer.PTR_OFFSET + index * Reflection.ARRAY_LONG_INDEX_SCALE);
    }
	}
}
