package com.verinite.assetmangementtool;
//
//import com.verinite.assetmangementtool.config.WarrentyMailSender;
//import com.verinite.assetmangementtool.entity.AssetsEntity;
//import com.verinite.assetmangementtool.repository.AssetsRepository;
////import org.junit.Test;
//import org.junit.jupiter.api.Test;
//
//
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.mail.MessagingException;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//import static org.mockito.Mockito.when;
//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
//public class WarrentyMailTest {
//
//    @InjectMocks
//    WarrentyMailSender warrentyMailSender;
//
//    @Mock
//    AssetsRepository repository;
//
//    @Test
//    public void getWarrentyExpiredAssets_test() throws MessagingException, UnsupportedEncodingException {
//        List<AssetsEntity> assetsEntityList = new ArrayList<>();
//        AssetsEntity assetEntity = dummyAssetsEntity();
//        assetsEntityList.add(assetEntity);
//        when(repository.findAll()).thenReturn(assetsEntityList);
//        warrentyMailSender.getWarrentyExpiredAssets();
//
//
//    }
//
//    private AssetsEntity dummyAssetsEntity(){
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
//
//
//
//}

//package com.verinite.assetmangementtool.testController;

import com.verinite.assetmangementtool.config.WarrentyMailSender;
import com.verinite.assetmangementtool.entity.AssetsEntity;
import com.verinite.assetmangementtool.repository.AssetsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@SpringBootTest
public class WarrentyMailTest {
    @Autowired
    WarrentyMailSender warrentyMailSender;

    @MockBean
    AssetsRepository repository;

    @Test
    public void getWarrentyExpiredAssets_test() throws MessagingException, UnsupportedEncodingException {
        List<AssetsEntity> assetsEntityList = new ArrayList<>();
        AssetsEntity assetEntity = dummyAssetsEntity();
        assetsEntityList.add(assetEntity);
        when(repository.findAll()).thenReturn(assetsEntityList);
        warrentyMailSender.getWarrentyExpiredAssets();


    }

    private AssetsEntity dummyAssetsEntity(){
        AssetsEntity assetEntity = new AssetsEntity();
        assetEntity.setAssetName("Laptop");
        assetEntity.setAddedBy("Ahalya");
        assetEntity.setStatus("UnAssigned");
        assetEntity.setModelName("Dell");
        assetEntity.setOperatingSystem("Linux");
        assetEntity.setPurchaseDate("06-09-2022");
        assetEntity.setWarrantyDate("30-09-2022");
        assetEntity.setSerialNumber("Dell1222");
        assetEntity.setLocation("Chennai");
        return assetEntity;
    }

}
