package com.Lee.bookstore_backend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.Lee.bookstore_backend.entity.OrderRecord;
import com.Lee.bookstore_backend.entity.OrderTable;
import com.Lee.bookstore_backend.service.OrderService;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
  final OrderService orderService;

  @Autowired
  OrderController(OrderService orderService){
    this.orderService=orderService;
  }


  @RequestMapping("/getOrder")
  public String getOrder(@RequestParam("user_id") Integer user_id) {
    System.out.println("get order receive: controller:"+user_id);
    List<OrderTable> orderTables = orderService.getOrder(user_id);
    return JSON.toJSONString(orderTables, SerializerFeature.DisableCircularReferenceDetect);
  }

  @RequestMapping("/getOrderItems")
  public List<OrderRecord> getOrderItems(@RequestParam("order_id") Integer order_id) {
    return orderService.getOrderItems(order_id);
  }

  @RequestMapping("/finishOrder")
  public void finishOrder(@RequestBody Map<String, Integer> paras) {
    Integer order_id = paras.get("orderId");
    orderService.finishOrder(order_id);
  }

  @RequestMapping("/getAllOrders")
  public String getAllOrders(@RequestBody Map<String, String> paras){

    String startStr = paras.get("start"), endStr = paras.get("end");

    Timestamp start = startStr.equals("null")
        ? (new Timestamp(1)) : Timestamp.valueOf(startStr);
    Timestamp end = endStr.equals("null") ?
        (new Timestamp(System.currentTimeMillis())) : Timestamp.valueOf(endStr);

    List<OrderTable> orderTables =  orderService.getAllOrders(start, end);

    return JSON.toJSONString(orderTables, SerializerFeature.DisableCircularReferenceDetect);
  }

  @RequestMapping("/getAllOrdersByUserId")
   String getAllOrdersByUserId(@RequestBody Map<String, String> paras){
    String startStr = paras.get("start"), endStr = paras.get("end");
    Integer userId = Integer.valueOf(paras.get("userId"));

    Timestamp start = startStr.equals("null")
        ? (new Timestamp(1)) : Timestamp.valueOf(startStr);

    Timestamp end = endStr.equals("null") ?
        (new Timestamp(System.currentTimeMillis())) : Timestamp.valueOf(endStr);

    List<OrderTable> orderTables = orderService.getAllOrdersByUserId(userId, start, end);
    return JSON.toJSONString(orderTables, SerializerFeature.DisableCircularReferenceDetect);
  }
}
