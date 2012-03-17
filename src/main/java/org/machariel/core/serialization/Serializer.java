package org.machariel.core.serialization;

import org.machariel.core.allocator.Allocator;
import org.machariel.core.util.Reflection;

public abstract class Serializer implements ISerializer {
  public static final long PTR_OFFSET = 2 * Reflection.ARRAY_INT_INDEX_SCALE;
  public static final Serializer DIRECT = new DirectSerializer(Allocator.DIRECT);
  public static final Serializer CHECKED = null;
}