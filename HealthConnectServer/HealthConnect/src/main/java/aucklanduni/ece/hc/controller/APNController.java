package aucklanduni.ece.hc.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import aucklanduni.ece.hc.repository.model.ApnUser;
import aucklanduni.ece.hc.service.ApnUserService;

@Controller
@RequestMapping("/ApnUser")
public class APNController {
	@Autowired
	private ApnUserService apnUserService;

	@RequestMapping(value="/showAll")
	public String showAll(HttpServletRequest request, HttpServletResponse response){
		System.out.println("show all");
		try {
			List<ApnUser> list = apnUserService.findAll();
			
			System.out.println(list.get(0).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apnUser/showAll";
	}

	@RequestMapping(value="/add")
	public String add(HttpServletRequest request, HttpServletResponse response){
		ApnUser t = new ApnUser();
		t.setCreateDate(new Date());
		t.setEmail("zyua826@aaa.com");
		t.setPassword("pwd");
		t.setUsername("ua"+(int)(Math.random()*100));
		try {
			apnUserService.add(t);
			
			System.out.println(t.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apnUser/add";
	}

	@RequestMapping(value="/update")
	public String update(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ApnUser t = apnUserService.findById(new Long(5));
		t.setEmail("updateEmail");
		
		try {
			apnUserService.update(t);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apnUser/delete";
	}

	@RequestMapping(value="/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response){

		try {
			apnUserService.deleteById(new Long(5));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apnUser/delete";
	}
	
}
