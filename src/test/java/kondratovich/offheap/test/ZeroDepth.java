package kondratovich.offheap.test;

import static org.junit.Assert.*;

import java.util.Random;

import kondratovich.offheap.core.allocator.Key;
import kondratovich.offheap.core.serialization.Serializer;
import kondratovich.offheap.test.data.Bean3;
import kondratovich.offheap.test.util.Common;

import org.junit.Test;

public class ZeroDepth {
  private Random r = new Random();
  
  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Bean3 bean0 = new Bean3();
    
    bean0.randomize();
    bean0._bean = null;
    bean0.s = null;
    
    Key ref = Serializer.DIRECT.serialize(bean0);
    Bean3 bean1 = (Bean3) Serializer.DIRECT.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
    
    ref.free();
  }
  
  @Test
  public void test2() throws InstantiationException {
    Object[] a = new Integer[r.nextInt(100) + 1];
    for (int i = 0; i < a.length; i++) a[i] = r.nextInt();
    
    Key ref = Serializer.DIRECT.serialize(a);
    Object[] b = (Object[]) Serializer.DIRECT.deserialize(ref);
    
    for (Object o : b) assertNull(o);
        
    ref.free();
  }
}
