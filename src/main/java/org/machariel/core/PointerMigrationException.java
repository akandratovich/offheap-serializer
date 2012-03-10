package org.machariel.core;

public class PointerMigrationException extends Exception {
  private static final long serialVersionUID = -1263463175875683627L;
  private final long ptr;
  
  public PointerMigrationException(long pointer) {
    ptr = pointer;
  }
  
  @Override
  public String getMessage() {
    return "Native pointer can't be dereferenced: 0x" + String.format("%1$02X", ptr);
  }
}