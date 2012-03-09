package org.machariel.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.machariel.core.manager.ReferenceSerializationManager;
import org.machariel.test.data.Bean3;
import org.machariel.test.util.Common;

public class Extends {

  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    ReferenceSerializationManager<Bean3> sm = ReferenceSerializationManager.acquire(Bean3.class);
    
    Bean3 bean0 = new Bean3();
    
    bean0.randomize();
    long ref = sm.serialize(bean0, 2);
    Bean3 bean1 = sm.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
  }
}
