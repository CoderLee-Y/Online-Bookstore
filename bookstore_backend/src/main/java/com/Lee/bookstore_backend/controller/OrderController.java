package com.Lee.bookstore_backend.controller;

import com.Lee.bookstore_backend.service.CartService;
import com.Lee.bookstore_backend.utils.messageUtils.Message;
import com.Lee.bookstore_backend.utils.messageUtils.MessageUtil;
import com.Lee.bookstore_backend.utils.sessionUtils.SessionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.Lee.bookstore_backend.entity.OrderRecord;
import com.Lee.bookstore_backend.entity.OrderTable;
import com.Lee.bookstore_backend.service.OrderService;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@RestController
public class OrderController {

  private WebApplicationContext webApplicationContext;
  final OrderService orderService;

  @Autowired
  OrderController(OrderService orderService, WebApplicationContext webApplicationContext) {
    this.orderService = orderService;
    this.webApplicationContext = webApplicationContext;
  }

  @RequestMapping("/getOrder")
  public String getOrder() {
    Integer user_id = Objects.requireNonNull(SessionUtil.getAuthority()).getInt("userId");
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
    Integer userId = Objects.requireNonNull(SessionUtil.getAuthority()).getInt("userId");

    Timestamp start = startStr.equals("null")
        ? (new Timestamp(1)) : Timestamp.valueOf(startStr);

    Timestamp end = endStr.equals("null") ?
        (new Timestamp(System.currentTimeMillis())) : Timestamp.valueOf(endStr);

    List<OrderTable> orderTables = orderService.getAllOrdersByUserId(userId, start, end);
    return JSON.toJSONString(orderTables, SerializerFeature.DisableCircularReferenceDetect);
  }

  //  在这里创建protoType的service分别获取Session以提高应对销售洪峰的能力
  @RequestMapping("/createOrder")
  public Message createOrder(
      @RequestParam("book_id") List<Long> book_id,
      @RequestParam("amount") List<Integer> amount,
      @RequestParam("price") List<BigDecimal> price) {
    CartService cartService = webApplicationContext.getBean(CartService.class);
    System.out.println(cartService);
    if (cartService.createOrder(book_id, amount, price) == 0) {
      return MessageUtil.createMessage(MessageUtil.ALREADY_LOGIN_CODE, "Success");
    } else {
      return MessageUtil.createMessage(MessageUtil.LOGIN_ERROR_CODE, "You don't have access");
    }
  }
}
