package kondratovich.offheap.test;

import static org.junit.Assert.*;
import kondratovich.offheap.core.allocator.Key;
import kondratovich.offheap.core.serialization.Serializer;
import kondratovich.offheap.test.data.Bean3;
import kondratovich.offheap.test.util.Common;

import org.junit.Test;

public class Extends {

  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Bean3 bean0 = new Bean3();
    
    bean0.randomize();
    Key ref = Serializer.DIRECT.serialize(bean0, 2);
    Bean3 bean1 = (Bean3) Serializer.DIRECT.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
    
    ref.free();
  }
}
