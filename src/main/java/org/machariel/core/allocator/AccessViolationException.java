package org.machariel.core.allocator;

public class AccessViolationException extends RuntimeException {
  private static final long serialVersionUID = -5668228761114670092L;
  
  private final long ptr;
  
  public AccessViolationException(long pointer) {
    ptr = pointer;
  }
  
  @Override
  public String getMessage() {
    return String.format("Access Violation %1$02X", ptr);
  }
}