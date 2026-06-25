package com.verinite.assetmanagementtool.service;

import com.verinite.assetmanagementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmanagementtool.repository.AdminRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsServie implements UserDetailsService {

    @Autowired
    AdminRegistrationRepository registerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AdminRegistrationEntity adminRegistrationEntity = (AdminRegistrationEntity) registerRepository.findByEmpId(username);
        if (adminRegistrationEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(adminRegistrationEntity.getEmpId(), adminRegistrationEntity.getPassword(), new ArrayList<>());

    }
}


