package com.Lee.bookstore_backend.multithreading;

import com.alibaba.fastjson.JSONObject;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class AddVisitors implements Callable<Integer> {

  /**
   *
   * @return visitors now
   */
  @Override
  public Integer call() {
    Visitors visitors = new Visitors();
    return visitors.addOne();
  }
}
