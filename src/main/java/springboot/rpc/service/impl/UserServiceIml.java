package springboot.rpc.service.impl;

import springboot.rpc.entity.User;
import springboot.rpc.service.UserService;

public class UserServiceIml implements UserService {
    @Override
    public User findUser() {
        User user = new User(1, "ys", "lsj");
        return user;
    }
}
