package org.machariel.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.machariel.core.manager.ReferenceSerializationManager;
import org.machariel.test.data.Bean2;
import org.machariel.test.util.Common;

public class NullReference {

  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
    ReferenceSerializationManager<Bean2> sm = ReferenceSerializationManager.acquire(Bean2.class);
    
    Bean2 bean0 = new Bean2();
    bean0.randomize();
    long ref = sm.serialize(bean0, 2);
    Bean2 bean1 = sm.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
  }
}