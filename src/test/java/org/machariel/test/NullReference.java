package org.machariel.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.machariel.core.allocator.Key;
import org.machariel.core.serialization.Serializer;
import org.machariel.test.data.Bean2;
import org.machariel.test.util.Common;

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