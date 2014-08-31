package aucklanduni.ece.hc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.service.DictionaryService;

import com.google.gson.Gson;

@Controller
@RequestMapping("/Dictionary")
public class RoleController {
	@Autowired
	private DictionaryService roleService;

	@RequestMapping(value="/showRoles")
	@ResponseBody
	public String showRoles(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="groupId",required=false) Long groupId,
			@RequestParam(value="accountId",required=false) Long accountId){
		String roles = null;
		Map<String, ArrayList<Dictionary>> rolesArray = new HashMap<String, ArrayList<Dictionary>>();
		ArrayList<Dictionary> roleList;
		System.out.println("showRoles");
		try {
			if(groupId == null || accountId == null) 
				roleList = roleService.GetRoles();
			else {
				roleList = roleService.GetSpecificRoles(accountId.longValue(), groupId.longValue());
			}
				
			rolesArray.put("roles", roleList);
			Gson gson = new Gson();
			System.out.println(gson.toJson(rolesArray));
			roles = gson.toJson(rolesArray);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return roles;
//		try {
//			response.getWriter().print(roles);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@RequestMapping(value="/showAll")
	public String showAll(HttpServletRequest request, HttpServletResponse response){
		System.out.println("show all");
		try {
			List<Dictionary> list = roleService.findAll();

			System.out.println(list.get(0).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apnUser/showAll";
	}

}
