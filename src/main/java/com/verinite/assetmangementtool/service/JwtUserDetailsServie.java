package com.verinite.assetmangementtool.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmangementtool.repository.AdminRegistrationRepository;

@Service
public class JwtUserDetailsServie implements UserDetailsService{

	@Autowired
	AdminRegistrationRepository registerRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		if(username.equals("V00293")) {
//			return new User("V00293", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",new ArrayList<>());
//		}else {
//			throw new UsernameNotFoundException("User not found with username:" + username);
//		}
//		
//		
		
		
		AdminRegistrationEntity adminRegistrationEntity = (AdminRegistrationEntity) registerRepository.findByEmpId(username);
		if(adminRegistrationEntity==null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		
		return new User(adminRegistrationEntity.getEmpId(),adminRegistrationEntity.getPassword(),new ArrayList<>());
		
	}
	}


