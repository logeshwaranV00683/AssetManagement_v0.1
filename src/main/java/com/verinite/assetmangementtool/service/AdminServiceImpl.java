package com.verinite.assetmangementtool.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

import com.verinite.assetmangementtool.dto.AdminRegistrationDto;
import com.verinite.assetmangementtool.service.mailservice.AdminPromotionMailer;
import org.modelmapper.ModelMapper;
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
import org.springframework.transaction.annotation.Transactional;

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
	ModelMapper modelMapper;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	private AdminPromotionMailer adminPromotionService;

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
		return new ResponseEntity(new JwtResponse<AdminRegistrationDto>(token, modelMapper.map(adminData,AdminRegistrationDto.class) ), HttpStatus.OK);

	}

	@Override
	public ResponseEntity updateAdminEntity(EmployeeEntity employee) {
		AdminRegistrationEntity adminRegistrationEntity = registerRepo.findByEmpId(employee.getEmpId());
		adminRegistrationEntity.setMail(employee.getMail());
		adminRegistrationEntity.setFirstName(employee.getFirstName());
		adminRegistrationEntity.setLastName(employee.getLastName());
		adminRegistrationEntity.setStatus(employee.getStatus());
		adminRegistrationEntity.setLocation(employee.getLocation());

		// update the existing admin registration
		registerRepo.save(adminRegistrationEntity);

		// Return a success response
		return ResponseEntity.status(HttpStatus.CREATED).body("Admin Updated successfully.");

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

	public void registerNewAdminWithoutPassword(EmployeeEntity employee)
	{
		AdminRegistrationEntity adminRegistrationEntity = new AdminRegistrationEntity();
		String password = generateComplexPassword(16);

		adminRegistrationEntity.setMail(employee.getMail());
		adminRegistrationEntity.setRole(employee.getRole());
		adminRegistrationEntity.setEmpId(employee.getEmpId());
		adminRegistrationEntity.setFirstName(employee.getFirstName());
		adminRegistrationEntity.setLastName(employee.getLastName());
		adminRegistrationEntity.setStatus(employee.getStatus());
		adminRegistrationEntity.setLocation(employee.getLocation());
		adminRegistrationEntity.setPassword(password);
		registerNewAdmin(adminRegistrationEntity);
		adminPromotionService.promoteToAdmin(modelMapper.map(adminRegistrationEntity,AdminRegistrationDto.class), password);

	}
	private String generateComplexPassword(int length) {
		if (length < 8) throw new IllegalArgumentException("Minimum length is 8");

		String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lower = "abcdefghijklmnopqrstuvwxyz";
		String digits = "0123456789";
		String special = "!@#$%^&*()-_=+[]{}|;:,.<>?";
		String all = upper + lower + digits + special;

		SecureRandom rand = new SecureRandom();
		StringBuilder password = new StringBuilder();

		// Ensure one character from each category
		password.append(upper.charAt(rand.nextInt(upper.length())));
		password.append(lower.charAt(rand.nextInt(lower.length())));
		password.append(digits.charAt(rand.nextInt(digits.length())));
		password.append(special.charAt(rand.nextInt(special.length())));

		// Fill remaining characters
		for (int i = 4; i < length; i++) {
			password.append(all.charAt(rand.nextInt(all.length())));
		}

		// Shuffle result
		return shuffleString(password.toString(), rand);
	}

	private static String shuffleString(String input, SecureRandom rand) {
		char[] a = input.toCharArray();
		for (int i = a.length - 1; i > 0; i--) {
			int j = rand.nextInt(i + 1);
			char temp = a[i];
			a[i] = a[j];
			a[j] = temp;
		}
		return new String(a);
	}

	@Transactional
	public ResponseEntity deleteAdmin(String empId) {
		EmployeeEntity employee = employeeRepository.findByEmpId(empId);
		registerRepo.deleteByEmpId(empId);
		employee.setRole("User");
		employeeRepository.save(employee);
		return ResponseEntity.status(HttpStatus.CREATED).body("Admin Deleted successfully.");
	}
}
