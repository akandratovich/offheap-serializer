package org.machariel.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import redis.clients.jedis.Jedis;
import sun.misc.Unsafe;

import com.os.cpnet.db.ConnectionFactory;
import com.os.cpnet.db.RedisConnector;
import com.os.cpnet.tool.F.F10;

public class Latency {
  private static final int warmup = 10000;
  private static final int run = 150;
  
  public static void main(String[] args) {
    long mysql[] = new long[run];
    long mysql_i[] = new long[run];
    long redis[] = new long[run];
    long local[] = new long[run];
    
    System.out.println("mysql ...");
    callMySQL(false, mysql);
    System.out.println("mysql indexed ...");
    callMySQL(true, mysql_i);
    System.out.println("redis ...");
    callRedis(redis);
    System.out.println("local ...");
    callLocal(local);
    
    System.out.println("sorting ...");
    Arrays.sort(mysql);
    Arrays.sort(mysql_i);
    Arrays.sort(redis);
    Arrays.sort(local);
    System.out.println("done.\n");
    
    for (int i = 0; i < 10; i++) {
      int index = i * run / 10;
      System.out.println(String.format("%1$d %2$d %3$d %4$d", mysql[index], mysql_i[index], redis[index], local[index]/10000));
    }
  }
  
  private static void callMySQL(boolean index, long[] time) {
    if (index) callMySQL("select attr_code, company_code, attr_type_code, name, container from attribute where attr_code = 6683", time);
    else callMySQL("select attr_code, company_code, attr_type_code, name, container from attribute where container = 'Evalaze'", time);
  }
  
  private static void callMySQL(String request, long[] time) {
    Connection c = null;
    PreparedStatement p = null;
    ResultSet r = null;
    
    try {
      c = ConnectionFactory.connection();
      p = c.prepareStatement(request);
      
      for (int i = 0; i < warmup; i++)
        r = p.executeQuery();
      
      for (int i = 0; i < run; i++) {
        long m0 = System.nanoTime();
//        for (int j = 0; j < 1000; j++) 
          r = p.executeQuery();
        long m1 = System.nanoTime();
        
        time[i] = m1 - m0;
      }
      
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      ConnectionFactory.close(c);
      ConnectionFactory.close(p);
      ConnectionFactory.close(r);
    }
  }
  
  private static void callRedis(final long[] time) {
    RedisConnector.with(new F10<RedisConnector>() {
      @Override
      public void perform(RedisConnector v) {
        Jedis cl = v.raw();
        for (int i = 0; i < warmup; i++)
          cl.get("cp_net:stdtree:plot");
        
        for (int i = 0; i < run; i++) {
          long m0 = System.nanoTime();
//          for (int j = 0; j < 1000; j++) 
            cl.get("cp_net:stdtree:plot");
          long m1 = System.nanoTime();
          
          time[i] = m1 - m0;
        }
      }
    });
  }
  
  private static void callLocal(final long[] time) {
    Random r = new Random();
    HashMap<Integer, Long> memory = new HashMap<Integer, Long>();
    
    for (int i = 0; i < warmup; i++) memory.put(r.nextInt(run), r.nextLong());
    
    for (int i = 0; i < warmup; i++)
      memory.get(r.nextInt(warmup));
    
    for (int i = 0; i < run; i++) {
      long m0 = System.nanoTime();
      for (int j = 0; j < 10000; j++) memory.get(r.nextInt(run));
      long m1 = System.nanoTime();
      
      time[i] = m1 - m0;
    }
  }
  
  Unsafe u = U.instance();
  
  public static final class U {
    private U() {}
    
    private static final sun.misc.Unsafe u = acquire();
    
    public static sun.misc.Unsafe instance() { return u; }
    
    private static sun.misc.Unsafe acquire() {
      sun.misc.Unsafe unsafe = null;
      try {
        java.lang.reflect.Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        unsafe = (sun.misc.Unsafe) field.get(null);
      } catch (Exception e) { throw new AssertionError(e); }
      return unsafe;
    }
  }
}
