package com.geek.okweb.controller.admin;

import com.geek.okweb.config.WebSecurityConfig;
import com.geek.okweb.domain.User;
import com.geek.okweb.form.UserForm;
import com.geek.okweb.service.UserService;
import com.geek.okweb.utils.MathUtil;
import com.geek.okweb.utils.Result;
import com.geek.okweb.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String login(Model model) {
		long width = MathUtil.getRandomNumberByRange();
		long height = MathUtil.getRandomNumberByRange();
		model.addAttribute("x", width);
		model.addAttribute("y", height);
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(WebSecurityConfig.USERID);
		return "redirect:/login";
	}

	@PostMapping("/login")
	@ResponseBody
	public Map<String, Object> login(String username, String password, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (username != null && username != "") {
			User user = userService.findByUserName(username);
			if (user != null) {
				Integer enable = user.getUsable();
				if (enable == null) {
					enable = 0;
				}
				if (enable == 1) {
					map.put("lusername", "该用户已被禁用");
					return map;
				}
				String pw = user.getPassword();
				if (!encoder.matches(password,pw)) { //判断用户输入的密码是否与加密的密码一致
					map.put("lpassword", "密码错误");
					return map;
				} else if (session.getAttribute("user") != null) {
					map.put("lusername", "请勿重复登录");
					return map;
				} else {
					session.setAttribute("user", user);
				}
			} else {
				map.put("lusername", "该账号不存在");
				return map;
			}
		} else {
			map.put("lusername", "请输入账号");
			return map;
		}
		return Result.toUrl("/admin/index");
	}

	@GetMapping("/changePassword")
	public String passwordPage(){
		return "admin/new_web/res_password";
	}

	@GetMapping("/error")
	public String error(){
		return "error";
	}

	@ResponseBody
	@PostMapping("/changePassword")
	@ResponseStatus(HttpStatus.OK)
	public Result changePassword(@Valid UserForm userForm, HttpSession session){
		log.info("【修改密码表单数据】={}",userForm.getOldPassword());
		log.info("【修改密码表单数据】={}",userForm.getNewPassword());
		log.info("【修改密码表单数据】={}",userForm.getConfirmPassword());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		User user = (User)session.getAttribute("user");
		if (user != null){
			if (encoder.matches(userForm.getOldPassword(),user.getPassword())){//原密码是否匹配
				if (StringUtils.equals(userForm.getNewPassword(),userForm.getConfirmPassword())){ //新密码是否确认密码一致
					//对新密码进行加密
					String encodePassword = encoder.encode(userForm.getNewPassword());
					user.setPassword(encodePassword);
					userService.update(user);
                    session.removeAttribute(WebSecurityConfig.USERID);
					log.info("【密码修改成功】={}={}",userForm.getNewPassword(),encodePassword);
					return ResultUtil.success("密码修改成功");
				}else {
					return ResultUtil.confirmPassword();
				}
			}else {
				return ResultUtil.fail("原密码错误");
			}

		}else {
			return ResultUtil.fail("用户不存在");
		}

	}

}
