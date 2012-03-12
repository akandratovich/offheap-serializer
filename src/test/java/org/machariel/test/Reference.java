package org.machariel.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;
import org.machariel.core.PointerMigrationException;
import org.machariel.core.serialization.Serializer;
import org.machariel.test.data.Bean0;
import org.machariel.test.data.Bean1;
import org.machariel.test.util.Common;

public class Reference {
  private Random r = new Random();
  
  @Test
  public void testGeneric() throws InstantiationException, IllegalArgumentException, IllegalAccessException, PointerMigrationException {
    Bean0 bean0 = new Bean1();
    
    bean0.randomize();
    long ref = Serializer.serialize(bean0, 2);
    Bean0 bean1 = (Bean0) Serializer.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
  }
  
  @Test
  public void test() throws InstantiationException, IllegalArgumentException, IllegalAccessException, PointerMigrationException {
    Bean1 bean0 = new Bean1();
    bean0.randomize();
    long ref = Serializer.serialize(bean0, 2);
    Bean1 bean1 = (Bean1) Serializer.deserialize(ref);
    
    assertTrue(Common.equal(bean0, bean1));
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testCollection() throws InstantiationException, IllegalArgumentException, IllegalAccessException, PointerMigrationException {
    ArrayList<Integer> col0 = new ArrayList<Integer>();
    for (int i = 0; i < r.nextInt(10); i++) col0.add(r.nextInt());
    
    long ref = Serializer.serialize(col0, 4);
    ArrayList<Integer> col1 = (ArrayList<Integer>) Serializer.deserialize(ref);
    
//    for (int i = 0; i < col0.size(); i++) {
//      System.out.println(col0.get(i) + " " + col1.get(i));
//    }
    
    assertTrue(Common.array_equal(col0.toArray(), col1.toArray()));
  }
}
