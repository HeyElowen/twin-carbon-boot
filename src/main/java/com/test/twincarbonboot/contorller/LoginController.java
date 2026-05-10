package com.test.twincarbonboot.contorller;

import com.test.twincarbonboot.mapper.UserMapper;
import com.test.twincarbonboot.pojo.LoginDTO;
import com.test.twincarbonboot.pojo.LoginVO;
import com.test.twincarbonboot.pojo.Result;
import com.test.twincarbonboot.pojo.SysUser;
import com.test.twincarbonboot.properties.CarbonProperties;
import com.test.twincarbonboot.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CarbonProperties carbonProperties;

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        SysUser user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null || !user.getPassword().equals(loginDTO.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        String token = JwtUtil.generateToken(user.getId(), user.getUsername());

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUsername(user.getUsername());

        LoginVO.Config config = new LoginVO.Config();
        config.setSceneUrl(carbonProperties.getSceneUrl());
        vo.setConfig(config);

        return Result.success(vo);
    }
}
