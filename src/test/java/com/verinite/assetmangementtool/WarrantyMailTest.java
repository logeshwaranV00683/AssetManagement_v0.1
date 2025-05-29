package com.verinite.assetmangementtool;

import com.verinite.assetmangementtool.config.WarrantyMailSender;
import com.verinite.assetmangementtool.repository.AssetsRepository;
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


//    private AssetsEntity dummyAssetsEntity() {
//        AssetsEntity assetEntity = new AssetsEntity();
//        assetEntity.setAssetName("Laptop");
//        assetEntity.setAddedBy("Ahalya");
//        assetEntity.setStatus("UnAssigned");
//        assetEntity.setModelName("Dell");
//        assetEntity.setOperatingSystem("Linux");
//        assetEntity.setPurchaseDate("06-09-2022");
//        assetEntity.setWarrantyDate("30-09-2022");
//        assetEntity.setSerialNumber("Dell1222");
//        assetEntity.setLocation("Chennai");
//        return assetEntity;
//    }

}
