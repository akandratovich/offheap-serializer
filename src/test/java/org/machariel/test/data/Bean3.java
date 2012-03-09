package org.machariel.test.data;

import java.util.Random;

public class Bean3 extends Bean2 {
	public int _int = 1;			// 3	4		30 + 8 + fill(8)
	public long _long = 2;			// 1	8
	public byte _byte = 3;			// 7	1
	public double _double = 4;		// 2	8
	public short _short = 5;		// 5	2
	public char _char = 'c';			// 6	2
	public boolean _boolean = true;	// 8	1
	public float _float = 8;		// 4	4
	public Bean2 _bean = null;
	
	public Bean3 randomize() {
	  Random r = new Random();
	  
	  _int = r.nextInt();
	  _long = r.nextLong();
	  _byte = (byte) (r.nextInt() % 255);
	  _double = r.nextDouble();
	  _short = (short) (r.nextInt() % (256 * 256 - 1));
	  _char = (char) (r.nextInt() % 255);
	  _boolean = r.nextBoolean();
	  _float = r.nextFloat();
	  
	  return this;
	}
}