package org.machariel.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.machariel.core.PointerMigrationException;
import org.machariel.core.serialization.Serializer;
import org.machariel.test.data.Bean3;
import org.machariel.test.util.Common;

public class Extends {

  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException, PointerMigrationException {
    Bean3 bean0 = new Bean3();
    
    bean0.randomize();
    long ref = Serializer.serialize(bean0, 2);
    Bean3 bean1 = (Bean3) Serializer.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
  }
}
