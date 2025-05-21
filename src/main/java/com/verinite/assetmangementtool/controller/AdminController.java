package com.verinite.assetmangementtool.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmangementtool.service.AdminServiceImpl;

@RestController
@RequestMapping("/assetManager/v1/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
public class AdminController {
	@Autowired
	AdminServiceImpl service;

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(AdminController.class);
	@PostMapping("/add/admin")
	public Object registerAdmin(@Valid @RequestBody AdminRegistrationEntity adminData) {
		// LOGGER.debug("inside method!!!: Register
		// method",adminData.getFirstName(),adminData.getLastName());
		return service.registerNewAdmin(adminData);
	}

	@GetMapping("/get/all")
	public List<AdminRegistrationEntity> getAll() {
		return service.getAll();
	}

//    @PostMapping("/login")
//    public ResponseEntity<?> adminLogin(@RequestBody AdminLoginDto login)
//    {
//        //LOGGER.info("inside method!!!: login method", login.getEmpId());
//       return service.checkLogin(login);
//    }
	// 176571974
	@DeleteMapping("/delete/{empId}")
	public Object delete(String empId){
		return service.deleteAdmin(empId);

	}

}
