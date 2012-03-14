package org.machariel.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.machariel.core.serialization.UnsafeSerializer;
import org.machariel.test.data.Bean0;
import org.machariel.test.util.Common;

public class Primitive {

  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Bean0 bean0 = new Bean0();
    bean0.randomize();
    long ref = UnsafeSerializer.serialize(bean0);
    Bean0 bean1 = (Bean0) UnsafeSerializer.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
  }
}
