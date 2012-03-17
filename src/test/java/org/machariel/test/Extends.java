package org.machariel.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.machariel.core.allocator.Key;
import org.machariel.core.serialization.Serializer;
import org.machariel.test.data.Bean3;
import org.machariel.test.util.Common;

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
