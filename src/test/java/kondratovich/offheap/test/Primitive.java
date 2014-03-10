package kondratovich.offheap.test;

import static org.junit.Assert.*;
import kondratovich.offheap.core.allocator.Key;
import kondratovich.offheap.core.serialization.Serializer;
import kondratovich.offheap.test.data.Bean0;
import kondratovich.offheap.test.util.Common;

import org.junit.Test;

public class Primitive {

  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Bean0 bean0 = new Bean0();
    bean0.randomize();
    
    Key ref = Serializer.DIRECT.serialize(bean0);
    Bean0 bean1 = (Bean0) Serializer.DIRECT.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
    
    ref.free();
  }
}
