package com.Lee.bookstore_backend.controller;


import com.Lee.bookstore_backend.entity.UserAuthority;
import com.Lee.bookstore_backend.service.UserService;
import com.Lee.bookstore_backend.utils.messageUtils.Message;
import com.Lee.bookstore_backend.utils.messageUtils.MessageUtil;
import com.Lee.bookstore_backend.utils.sessionUtils.SessionUtil;
import java.util.Map;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    final UserService userService;

    @Autowired
    LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/checkSession")
    public Message checkSession() {
        JSONObject authority = SessionUtil.getAuthority();
        if (authority != null) {
            return MessageUtil.createMessage(MessageUtil.ALREADY_LOGIN_CODE, MessageUtil.ALREADY_LOGIN_MSG);
        } else
            return MessageUtil.createMessage(MessageUtil.NOT_LOGIN_CODE, MessageUtil.NOT_LOGIN_MSG);
    }

    @RequestMapping("/checkAdmin")
    public Message checkAdmin() {
        JSONObject authority = SessionUtil.getAuthority();
        if (authority != null && authority.getInt("userIdentity") == 1) {
            return MessageUtil.createMessage(MessageUtil.ALREADY_LOGIN_CODE, MessageUtil.ALREADY_LOGIN_MSG);
        } else
            return MessageUtil.createMessage(MessageUtil.NOT_LOGIN_CODE, MessageUtil.NOT_LOGIN_MSG);
    }

    @RequestMapping("/Login")
    public Message login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String userPassword = params.get("password");

        UserAuthority userAuthority = userService.checkAuthority(username, userPassword);
        if (userAuthority == null) {
            return MessageUtil.createMessage(MessageUtil.LOGIN_ERROR_CODE, MessageUtil.LOGIN_ERROR_MSG);
        } else if(userAuthority.getUserIdentity() == -4){
            return MessageUtil.createMessage(MessageUtil.LOGIN_FORBIDDEN_CODE, MessageUtil.LOGIN_FORBIDDEN);
        }
        else {
            JSONObject newSession = new JSONObject();
            newSession.put("userId", userAuthority.getUserId());
            newSession.put("username", userAuthority.getUsername());
            newSession.put("userIdentity", userAuthority.getUserIdentity());
            SessionUtil.setSession(newSession);
            JSONObject responseData = JSONObject.fromObject(userAuthority);
            responseData.remove("userPassword");
            return MessageUtil.createMessage(MessageUtil.LOGIN_SUCCESS_CODE, MessageUtil.LOGIN_SUCCESS_MSG, responseData);
        }
    }

    @RequestMapping("/getAuth")
    public JSONObject getAuth() {
        return SessionUtil.getAuthority();
    }

    @RequestMapping("/logout")
    public Message logout() {
        boolean status = SessionUtil.removeSession();
        if (!status) {
            return MessageUtil.createMessage(MessageUtil.LOGOUT_ERROR_CODE, MessageUtil.LOGOUT_ERROR_MSG);
        } else
            return MessageUtil.createMessage(MessageUtil.LOGOUT_SUCCESS_CODE, MessageUtil.LOGOUT_SUCCESS_MSG);
    }
}
