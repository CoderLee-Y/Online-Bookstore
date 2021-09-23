package com.Lee.bookstore_backend.interceptor;

import com.Lee.bookstore_backend.utils.messageUtils.returnMessage;
import com.Lee.bookstore_backend.utils.messageUtils.MessageUtil;
import com.Lee.bookstore_backend.utils.sessionUtils.SessionUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionValidateInterceptor extends HandlerInterceptorAdapter {
    public SessionValidateInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        boolean status = SessionUtil.checkAuthority();

        if (!status) {
            System.out.println("Failed to pass auth check in Interceptor.");
            returnMessage returnMessage = MessageUtil.createMessage(MessageUtil.NOT_LOGIN_CODE, MessageUtil.NOT_LOGIN_MSG);
            this.sendJsonBack(response, returnMessage);
            return false;
        } else {
            return true;
        }
    }

    private void sendJsonBack(HttpServletResponse response, returnMessage returnMessage) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JSONObject.fromObject(returnMessage));
        } catch (IOException e) {
            System.out.println("send json back error");
        }
    }
}

