package com.Lee.bookstore_backend.controller;


import com.Lee.bookstore_backend.dto.BigFans;
import com.Lee.bookstore_backend.entity.User;
import com.Lee.bookstore_backend.entity.UserAuthority;
import com.Lee.bookstore_backend.service.UserService;
import com.Lee.bookstore_backend.utils.sessionUtils.SessionUtil;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/checkAuthority")
    UserAuthority checkAuthority(@RequestParam("userAccount") String userAccount,
        @RequestParam("userPassword") String userPassword) {
        return userService.checkAuthority(userAccount, userPassword);
    }

    @RequestMapping("/getUser")
    User getUser() {
        return userService.getUser();
    }

    @RequestMapping("/getUserById")
    User getUserById(@RequestParam("userId") Integer userId) {
        return userService.getUserById(userId);
    }

    @RequestMapping("/getCustomers")
    List<User> getCustomers() {
        System.out.println(userService.getCustomer().toString());
        return userService.getCustomer();
    }

    @RequestMapping("/getAdmins")
    List<UserAuthority> getAdmins() {
        System.out.println(userService.getAdmins().toString());
        return userService.getAdmins();
    }

    @RequestMapping("/changeUserMode")
    void changeUserMode(@RequestParam("userId") Integer userId) {
        System.out.println(userId);
        userService.changeMode(userId);
    }

    @RequestMapping("/register")
    void register(@RequestBody Object object) {
        userService.register(object);
    }

    @RequestMapping("/changeReceipt")
    void changeReceipt(@RequestBody Object object) {
        userService.changeReceipt(object);
    }

    @RequestMapping("/getReceipt")
    JSONObject getReceipt() {
        JSONObject user = SessionUtil.getAuthority();
        if(user == null)
            return null;
        return userService.getReceipt(user.getInt("userId"));
    }


    @RequestMapping("/getUserNow")
    void getUserNow(){
        JSONObject jsonObject = SessionUtil.getAuthority();
        assert jsonObject != null;
    }


    @RequestMapping("/isDupName")
    boolean isDupName(@RequestBody Object object){
        JSONObject jsonObject = JSONObject.fromObject(object).getJSONObject("file");
        String username = jsonObject.getString("email");
        return userService.isDupName(username);
    }

    @RequestMapping("/testDupName")
    boolean testDupName(@RequestBody Map<String, String> paras){
        String username = paras.get("email");
        return userService.isDupName(username);
    }

    @RequestMapping("/bigFans")
    List<BigFans> bigFans(@RequestBody Map<String, String> paras){
        String startStr = paras.get("start"), endStr = paras.get("end");

        Timestamp start = startStr.equals("null")
            ? (new Timestamp(1)) : Timestamp.valueOf(startStr);
        Timestamp end = endStr.equals("null") ?
            (new Timestamp(System.currentTimeMillis())) : Timestamp.valueOf(endStr);
        return userService.getBigFans(start, end);
    }
}
