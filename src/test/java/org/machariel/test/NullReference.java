package org.machariel.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.machariel.core.serialization.UnsafeSerializer;
import org.machariel.test.data.Bean2;
import org.machariel.test.util.Common;

public class NullReference {

  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Bean2 bean0 = new Bean2();
    bean0.randomize();
    long ref = UnsafeSerializer.serialize(bean0, 2);
    Bean2 bean1 = (Bean2) UnsafeSerializer.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
  }
}