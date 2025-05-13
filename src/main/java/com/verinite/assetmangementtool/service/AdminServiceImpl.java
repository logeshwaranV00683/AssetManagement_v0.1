package com.verinite.assetmangementtool.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.verinite.assetmangementtool.config.JwtTokenUtil;
import com.verinite.assetmangementtool.dto.AdminLoginDto;
import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmangementtool.entity.EmployeeEntity;
import com.verinite.assetmangementtool.model.JwtResponse;
import com.verinite.assetmangementtool.repository.AdminRegistrationRepository;
//import com.verinite.assetmangementtool.service.mailservice.OTPMailer;
//import net.bytebuddy.utility.RandomString;
import com.verinite.assetmangementtool.repository.EmployeeRepository;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	AdminRegistrationRepository registerRepo;
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	@Autowired
	JwtUserDetailsServie jwtUserDetailsServie;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public ResponseEntity<String> registerNewAdmin(AdminRegistrationEntity registration) {
		// Set admin role, encode password, and other default properties
		registration.setRole("admin");
		String encodedPassword = encoder.encode(registration.getPassword());
		registration.setPassword(encodedPassword);
		registration.setStatus("active");
		registration.setEmpId(registration.getEmpId().toUpperCase());

		// Check if the admin already exists
		if (registerRepo.existsByEmpId(registration.getEmpId())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin already exists.");
		}

		// Check if the employee exists
		EmployeeEntity employeeEntity = employeeRepository.findByEmpId(registration.getEmpId());
		if (employeeEntity == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee does not exist.");
		}

		// Check if the employee is already an admin
		if ("Admin".equalsIgnoreCase(employeeEntity.getRole())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee is already an admin.");
		}

		// Set the employee's role to "Admin" and save the updated employee entity
		employeeEntity.setRole("Admin");
		employeeRepository.save(employeeEntity);

		// Save the new admin registration
		registerRepo.save(registration);

		// Return a success response
		return ResponseEntity.status(HttpStatus.CREATED).body("Admin registered successfully.");
	}

	@Override
	public List<AdminRegistrationEntity> getAll() {
		return registerRepo.findAll();
	}

	@Override
	public ResponseEntity<?> checkLogin(AdminLoginDto login) throws Exception {

		AdminRegistrationEntity adminData = (AdminRegistrationEntity) registerRepo.findByEmpId(login.getEmpId());
		authenticate(login.getEmpId(), login.getPassword());
		final UserDetails userDetails = jwtUserDetailsServie.loadUserByUsername(adminData.getEmpId());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return new ResponseEntity(new JwtResponse(token), HttpStatus.OK);

	}

	public AdminRegistrationEntity getById(String empId) {
		return registerRepo.findByEmpId(empId);

	}

	public String updateAdmin(String id) {
		AdminRegistrationEntity adminRegistrationEntity = registerRepo.findByEmpId(id);
		adminRegistrationEntity.setStatus("active");
		registerRepo.save(adminRegistrationEntity);
		return "Updated Successfully";
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("User Disabled", e);
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid Credentials", e);
		}

	}

}
