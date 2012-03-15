package org.machariel.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.machariel.core.serialization.UnsafeSerializer;
import org.machariel.test.data.Bean3;
import org.machariel.test.util.Common;

public class Extends {

  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    Bean3 bean0 = new Bean3();
    
    bean0.randomize();
    long ref = UnsafeSerializer.serialize(bean0, 2);
    
//    Common.dump(bean0, 80);
//    System.out.println();
//    Common.dump(ref, 80);
    
    Bean3 bean1 = (Bean3) UnsafeSerializer.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
  }
}
