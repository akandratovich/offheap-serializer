package org.machariel.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.machariel.core.manager.ReferenceSerializationManager;
import org.machariel.test.data.Bean0;
import org.machariel.test.util.Common;

public class Primitive {

  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    ReferenceSerializationManager<Bean0> sm = ReferenceSerializationManager.acquire(Bean0.class);
    
    Bean0 bean0 = new Bean0();
    bean0.randomize();
    long ref = sm.serialize(bean0);
    Bean0 bean1 = sm.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
  }
}
