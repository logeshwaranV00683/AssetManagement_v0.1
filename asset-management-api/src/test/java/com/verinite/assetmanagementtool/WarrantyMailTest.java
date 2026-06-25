package com.verinite.assetmanagementtool;

import com.verinite.assetmanagementtool.config.WarrantyMailSender;
import com.verinite.assetmanagementtool.repository.AssetsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")

public class WarrantyMailTest {
    @Autowired
    WarrantyMailSender warrantyMailSender;

    @MockBean
    AssetsRepository repository;

    @Test
    public void getWarrantyExpiredAssets_test() throws MessagingException, UnsupportedEncodingException {
        warrantyMailSender.getWarrantyExpiredAssets();
    }


}
