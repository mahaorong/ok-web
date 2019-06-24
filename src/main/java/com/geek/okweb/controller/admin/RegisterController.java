package com.geek.okweb.controller.admin;

import com.geek.okweb.domain.User;
import com.geek.okweb.service.UserService;
import com.geek.okweb.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.validation.Valid;

@Slf4j
@Controller
public class RegisterController {

	@Autowired
	private UserService userService;

/*	@GetMapping("/register")
	public String register() {
		return "register";
	}*/

	/*@PostMapping("/register")
	@ResponseBody
	public Map<String,Object> register(@Valid User user,String password,
			String qpassword,String username){
		Map<String,Object> map=new HashMap<String,Object>();
		user=userService.findByUserName(username);
		if(!StringUtils.equals(password, qpassword)) {
			map.put("qpassword", "密码不一致");
			return map;
		}else if(user!=null) {
			map.put("username", "该用户名已存在");
			return map;
		}else {
			User user1=new User();
			user1.setId(KeyUtil.UUID());

			//加密用户密码
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodePassword = encoder.encode(password);

            user1.setPassword(encodePassword);
			user1.setUsername(username);
			userService.save(user1);
		}
		return Result.toUrl("/login");
	}*/


	@GetMapping("/registerAuto")
	@ResponseBody
	public String registerAuto(@Valid User user,String password,
							   String qpassword,String username){
		log.info(password+username+qpassword);
		user = userService.findByUserName(user.getUsername());
		if(!StringUtils.equals(password,qpassword)) {
			return "密码不一致";
		}else if(user!=null) {
			return "该用户名已存在";
		}else {
			User user1=new User();
			user1.setId(KeyUtil.UUID());
			//加密用户密码
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodePassword = encoder.encode(password);
			user1.setPassword(encodePassword);
			user1.setUsername(username);
			userService.save(user1);
		}
		return "success";
	}

}
