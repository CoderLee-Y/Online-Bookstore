package com.example.bookstore_backend.interceptor;

import com.example.bookstore_backend.utils.messageUtils.Message;
import com.example.bookstore_backend.utils.messageUtils.MessageUtil;
import com.example.bookstore_backend.utils.sessionUtils.SessionUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionValidateInterceptor extends HandlerInterceptorAdapter {
    public SessionValidateInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        boolean status = SessionUtil.checkAuthority();

        if (!status) {
            System.out.println("Failed");
            Message message = MessageUtil.createMessage(MessageUtil.NOT_LOGIN_CODE, MessageUtil.NOT_LOGIN_MSG);
            this.sendJsonBack(response, message);
            return false;
        } else {
            System.out.println("Success");
            return true;
        }
    }

    private void sendJsonBack(HttpServletResponse response, Message message) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JSONObject.fromObject(message));
        } catch (IOException e) {
            System.out.println("send json back error");
        }
    }
}

