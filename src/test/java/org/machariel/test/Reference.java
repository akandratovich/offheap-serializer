package org.machariel.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;
import org.machariel.core.access.ObjectAccessor;
import org.machariel.core.allocator.Key;
import org.machariel.core.serialization.Serializer;
import org.machariel.test.data.Bean0;
import org.machariel.test.data.Bean1;
import org.machariel.test.util.Common;

public class Reference {
  private Random r = new Random();
  
  @Test
  public void testGeneric() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Bean0 bean0 = new Bean1();
    
    bean0.randomize();
    Key ref = Serializer.DIRECT.serialize(bean0, 2);
    Bean0 bean1 = (Bean0) Serializer.DIRECT.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
    
    ref.free();
  }
  
  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Bean1 bean0 = new Bean1();
    bean0.randomize();
    Key ref = Serializer.DIRECT.serialize(bean0, 2);
    Bean1 bean1 = (Bean1) Serializer.DIRECT.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
    
    ref.free();
  }
  
  @Test
  public void test2() throws InstantiationException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
    Bean1 bean0 = new Bean1();
    bean0.randomize();
    Key ref = Serializer.DIRECT.serialize(bean0, 2);
    
    ObjectAccessor<Bean1> oa = new ObjectAccessor<Bean1>(bean0, false);
    
    for (int i = 0; i < 10; i++) {
      assertEquals(oa.getBoolean(ref, "_boolean"), bean0._boolean);
      assertEquals(oa.getByte(ref, "_byte"), bean0._byte);
      assertEquals(oa.getChar(ref, "_char"), bean0._char);
      assertEquals(oa.getDouble(ref, "_double"), bean0._double, 0.1);
      assertEquals(oa.getFloat(ref, "_float"), bean0._float, 0.1);
      assertEquals(oa.getInt(ref, "_int"), bean0._int);
      assertEquals(oa.getLong(ref, "_long"), bean0._long);
      assertEquals(oa.getShort(ref, "_short"), bean0._short);
    }
    
    for (int i = 0; i < 10; i++) {
      boolean _boolean = r.nextBoolean();
      bean0._boolean = _boolean;
      oa.putBoolean(ref, "_boolean", _boolean);
      
      byte _byte = (byte) (r.nextInt() % 255);
      bean0._byte = _byte;
      oa.putByte(ref, "_byte", _byte);
      
      char _char = (char) (r.nextInt() % 255);
      bean0._char = _char;
      oa.putChar(ref, "_char", _char);
      
      double _double = r.nextDouble();
      bean0._double = _double;
      oa.putDouble(ref, "_double", _double);
      
      float _float = r.nextFloat();
      bean0._float = _float;
      oa.putFloat(ref, "_float", _float);
      
      int _int = r.nextInt();
      bean0._int = _int;
      oa.putInt(ref, "_int", _int);
      
      long _long = r.nextLong();
      bean0._long = _long;
      oa.putLong(ref, "_long", _long);
      
      short _short = (short) (r.nextInt() % 255);
      bean0._short = _short;
      oa.putShort(ref, "_short", _short);
    }
    
    for (int i = 0; i < 10; i++) {
      Key vref = oa.getMember(ref, "_bean0");
      Bean0 value1 = (Bean0) Serializer.DIRECT.deserialize(vref);
      assertTrue(Common.equal(bean0._bean0, value1));
    }
    
    Bean1 bean1 = (Bean1) Serializer.DIRECT.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
    
    ref.free();
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testCollection() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    ArrayList<Integer> col0 = new ArrayList<Integer>();
    for (int i = 0; i < r.nextInt(10); i++) col0.add(r.nextInt());
    
    Key ref = Serializer.DIRECT.serialize(col0, 4);
    ArrayList<Integer> col1 = (ArrayList<Integer>) Serializer.DIRECT.deserialize(ref);
    
    assertTrue(Common.array_equal(col0.toArray(), col1.toArray()));
    
    ref.free();
  }
}
