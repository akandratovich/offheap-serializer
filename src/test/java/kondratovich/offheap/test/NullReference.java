package kondratovich.offheap.test;

import static org.junit.Assert.*;
import kondratovich.offheap.core.allocator.Key;
import kondratovich.offheap.core.serialization.Serializer;
import kondratovich.offheap.test.data.Bean2;
import kondratovich.offheap.test.util.Common;

import org.junit.Test;

public class NullReference {

  @Test
  public void test() throws Exception {
    Bean2 bean0 = new Bean2();
    bean0.randomize();
    Key ref = Serializer.DIRECT.serialize(bean0, 2);
    Bean2 bean1 = (Bean2) Serializer.DIRECT.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
    
    ref.free();
  }
}