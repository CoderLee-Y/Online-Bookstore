package com.example.bookstore_backend.serviceimpl;

import com.example.bookstore_backend.dao.OrderDao;
import com.example.bookstore_backend.dao.UserDao;
import com.example.bookstore_backend.dto.BigFans;
import com.example.bookstore_backend.entity.OrderRecord;
import com.example.bookstore_backend.entity.OrderTable;
import com.example.bookstore_backend.entity.User;
import com.example.bookstore_backend.entity.UserAuthority;
import com.example.bookstore_backend.service.UserService;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImplement implements UserService {
    final UserDao userDao;
    final OrderDao orderDao;

    @Autowired
    UserServiceImplement(UserDao userDao, OrderDao orderDao){
        this.userDao = userDao;
        this.orderDao = orderDao;
    }

    @Override
    public UserAuthority checkAuthority(String username, String userPassword) {
        UserAuthority userAuthority = userDao.checkAuthority(username, userPassword);
        if(userAuthority == null)
            return null;

        if(userAuthority.getUserIdentity() == 1)
            return userAuthority;

        User user = userDao.getUserById(userAuthority.getUserId());
        if(user.getAbandon()==1)
            userAuthority.setUserIdentity(-4);


        return userAuthority;
    }

    @Override
    public User getUser() {
        return userDao.getUser();
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public List<User> getCustomer() {
        return userDao.getCustomer();
    }

    @Override
    public List<UserAuthority> getAdmins() {
        List<UserAuthority> userAuthorityList =  userDao.getAdmins();
        for(UserAuthority userAuthority: userAuthorityList){
            userAuthority.setUserPassword("Removed for privacy");
        }
        return userAuthorityList;
    }

    @Override
    public void changeMode(Integer userId) {
        userDao.changeMode(userId);
    }

    @Override
    public void register(Object object) {
        userDao.register(object);
    }

    @Override
    public void changeReceipt(Object object) {
        userDao.changeReceipt(object);
    }

    @Override
    public JSONObject getReceipt(Integer userId) {
        User user = userDao.getUserById(userId);
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("address", user.getAddress());
        jsonObject.put("tel", user.getTel());
        jsonObject.put("name", user.getNickname());
        return jsonObject;
    }

    @Override
    public boolean isDupName(String name) {
        return userDao.isDupName(name);
    }

    @Override
    public List<BigFans> getBigFans(Timestamp start, Timestamp end) {
        List<User> users = userDao.getCustomer();
        List<BigFans> bigFans = new ArrayList<>();

        for(User user: users){
            List<OrderTable> orderTables = orderDao.getAllByUser(user);
            BigFans bigFan = new BigFans(user, 0, new BigDecimal(0));
            for(OrderTable order:orderTables){
                if(order.getOrderTime().after(end) || order.getOrderTime().before(start)){
                    continue;
                }
                List<OrderRecord> items = order.getItems();
                for(OrderRecord orderRecord: items){
                    bigFan.setAmount(bigFan.getAmount()+orderRecord.getAmount());
                    bigFan.setValue(bigFan.getValue().add(orderRecord.getPrice()
                        .multiply(new BigDecimal(orderRecord.getAmount()))));
                }
            }
            bigFans.add(bigFan);
        }
        bigFans.sort(Comparator.comparing(BigFans::getValue).reversed());
        return bigFans;
    }
}
