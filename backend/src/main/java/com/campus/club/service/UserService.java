package com.campus.club.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campus.club.common.BusinessException;
import com.campus.club.dto.LoginDTO;
import com.campus.club.dto.LoginResponse;
import com.campus.club.dto.RegisterDTO;
import com.campus.club.dto.UserInfo;
import com.campus.club.entity.User;
import com.campus.club.mapper.UserMapper;
import com.campus.club.utils.JwtUtils;
import com.campus.club.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    public void register(RegisterDTO dto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", dto.getUsername());
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setName(dto.getName());
        user.setStudentNo(dto.getStudentNo());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.insert(user);
    }

    public LoginResponse login(LoginDTO dto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", dto.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        String md5Password = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!md5Password.equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());

        UserInfo userInfo = new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getStudentNo(),
                user.getPhone(),
                user.getRole(),
                user.getCreatedAt()
        );

        return new LoginResponse(token, userInfo);
    }

    public UserInfo getCurrentUser() {
        Long userId = UserContext.getUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(401, "用户不存在");
        }
        return new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getStudentNo(),
                user.getPhone(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }
}
