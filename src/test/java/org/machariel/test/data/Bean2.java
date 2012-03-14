package org.machariel.test.data;

import java.util.Random;

public class Bean2 extends Bean0 {
  private static final long serialVersionUID = 3918682416865076385L;
  public int _int = 1;			// 3	4		30 + 8 + fill(8)
	public long _long = 2;			// 1	8
	public byte _byte = 3;			// 7	1
	public double _double = 4;		// 2	8
	public short _short = 5;		// 5	2
	public char _char = 'c';			// 6	2
	public boolean _boolean = true;	// 8	1
	private float _float1 = 8;		// 4	4
	public Bean0 _bean = null;
	
	public Bean2 randomize() {
	  Random r = new Random();
	  
	  _int = r.nextInt();
	  _long = r.nextLong();
	  _byte = (byte) (r.nextInt() % 255);
	  _double = r.nextDouble();
	  _short = (short) (r.nextInt() % (256 * 256 - 1));
	  _char = (char) (r.nextInt() % 255);
	  _boolean = r.nextBoolean();
	  _float1 = r.nextFloat() + _float1;
	  
	  return this;
	}
}