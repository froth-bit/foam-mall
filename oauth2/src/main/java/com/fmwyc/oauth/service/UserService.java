package com.fmwyc.oauth.service;

import com.fmwyc.oauth.pojo.User;
import com.fmwyc.oauth.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//配置自定义的User类
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if (user!=null){
            //指定应用的用户名，密码
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getPasswd(),
                    AuthorityUtils.createAuthorityList(user.getPasswd())
            );
        }
        throw new UsernameNotFoundException("User not found!");
    }


}
