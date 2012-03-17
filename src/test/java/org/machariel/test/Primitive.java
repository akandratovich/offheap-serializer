package org.machariel.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.machariel.core.allocator.Key;
import org.machariel.core.serialization.Serializer;
import org.machariel.test.data.Bean0;
import org.machariel.test.util.Common;

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
