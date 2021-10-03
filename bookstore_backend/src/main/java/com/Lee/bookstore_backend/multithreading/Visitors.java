package com.Lee.bookstore_backend.multithreading;

import java.util.concurrent.atomic.AtomicInteger;

public class Visitors {

  private static final AtomicInteger visitors = new AtomicInteger(0);

  public Integer addOne() {
    return visitors.addAndGet(1);
  }
}
