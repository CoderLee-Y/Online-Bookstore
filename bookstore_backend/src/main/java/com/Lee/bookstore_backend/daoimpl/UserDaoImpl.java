package com.Lee.bookstore_backend.daoimpl;


import com.Lee.bookstore_backend.dao.UserDao;
import com.Lee.bookstore_backend.entity.User;
import com.Lee.bookstore_backend.entity.UserAuthority;
import com.Lee.bookstore_backend.repository.UserAuthorityRepository;
import com.Lee.bookstore_backend.repository.UserRepository;
import com.Lee.bookstore_backend.utils.sessionUtils.SessionUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Repository
public class UserDaoImpl implements UserDao {
    UserAuthorityRepository userAuthorityRepository;
    UserRepository userRepository;

    @Autowired
    public void setUserAuthorityRepository(UserAuthorityRepository userAuthorityRepository) {
        this.userAuthorityRepository = userAuthorityRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserAuthority checkAuthority(String username, String userPassword) {
        return userAuthorityRepository.checkAuthority(username, userPassword);
    }

    @Override
    public User getUser() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpSession session = request.getSession(false);
            if (session != null) {
                Integer userId = (Integer) session.getAttribute("userId");
                return userRepository.getOne(userId);
            }
        }
        return null;
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepository.getOne(userId);
    }

    @Override
    public List<User> getCustomer() {
        return userRepository.findAll();
    }

    @Override
    public List<UserAuthority> getAdmins() {
        return userAuthorityRepository.findAllByUserIdentity(1);
    }

    @Override
    public void changeMode(Integer userId) {
        User user = userRepository.getOne(userId);
        Integer abandon = user.getAbandon();
        if (abandon == 1) {
            user.setAbandon(0);
        } else {
            user.setAbandon(1);
        }
        userRepository.saveAndFlush(user);
    }

    @Override
    public void register(Object parent_object) {
        JSONObject jsonObject = JSONObject.fromObject(parent_object).getJSONObject("file");
        String password = jsonObject.getString("password");
        String username = jsonObject.getString("email");
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setUserIdentity(0);
        userAuthority.setUserPassword(password);
        userAuthority.setUsername(username);
        userAuthority = userAuthorityRepository.saveAndFlush(userAuthority);

        User user = new User();
        user.setUserId(userAuthority.getUserId());
        user.setAddress(jsonObject.getString("address"));
        user.setEmail(username);
        user.setNickname(jsonObject.getString("nickname"));
        user.setTel(jsonObject.getString("phone"));
        user.setIcon("");
        user.setAbandon(0);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void changeReceipt(Object object) {
        JSONObject jsonObject = JSONObject.fromObject(object).getJSONObject("file");
        String address = jsonObject.getString("address");
        String tel = jsonObject.getString("phone");
        String name = jsonObject.getString("nickname");

        JSONObject jsonObject1 = SessionUtil.getAuthority();
        if(jsonObject1 != null){
            Integer userId = jsonObject1.getInt("userId");
            User user = userRepository.getOne(userId);
            user.setTel(tel);
            user.setNickname(name);
            user.setAddress(address);
            userRepository.saveAndFlush(user);
        }
    }

    @Override
    public boolean isDupName(String name) {
        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAllByUsername(name);
        System.out.println(userAuthorityList.size());
        return (userAuthorityList.size() >= 1);
    }
}
