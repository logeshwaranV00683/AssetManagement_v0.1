package com.verinite.assetmangementtool.config;

import com.verinite.assetmangementtool.entity.AssetsEntity;
import com.verinite.assetmangementtool.repository.AssetsRepository;
import com.verinite.assetmangementtool.service.mailservice.NotificationMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Configuration
public class WarrentyMailSender {

   @Autowired
   private AssetsRepository repository;

   @Autowired
   private NotificationMail notificationMail;

    @Scheduled(cron = "0 18 14 * * *")
    public void getWarrentyExpiredAssets() throws MessagingException, UnsupportedEncodingException {
        List<AssetsEntity> lessThanNintyDays = new ArrayList<>();
        List<AssetsEntity> assetsEntityList = repository.findAll();
        for (AssetsEntity entity : assetsEntityList) {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(entity.getWarrantyDate());
            Date date1 = null;
            try {
                date1 = date.parse(entity.getWarrantyDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(date1);

            System.out.println(ChronoUnit.DAYS.between(new Date().toInstant(), date1.toInstant()));
            long numberOfDays = ChronoUnit.DAYS.between(new Date().toInstant(), date1.toInstant());
            if (numberOfDays == 90 ) {
                System.out.println("Success");
                lessThanNintyDays.add(entity);
               // notificationMail.nofiyMailer("ahalya.chandrasekaran@verinite.com",lessThanNintyDays);
            }
            if (numberOfDays == 60 ) {
                System.out.println("Success");
                lessThanNintyDays.add(entity);
               // notificationMail.nofiyMailer("ahalya.chandrasekaran@verinite.com",lessThanNintyDays);
            }
            if (numberOfDays == 30 ) {
                System.out.println("Success");
                lessThanNintyDays.add(entity);
                //notificationMail.nofiyMailer("ahalya.chandrasekaran@verinite.com",lessThanNintyDays);
            }
            if(numberOfDays <= 15 && numberOfDays >=1){
                System.out.println("Success");
                lessThanNintyDays.add(entity);
                //notificationMail.nofiyMailer("ahalya.chandrasekaran@verinite.com",lessThanNintyDays);
            }

        }

        if(0!=lessThanNintyDays.size() && null!= lessThanNintyDays){
            notificationMail.nofiyMailer("ahalya.chandrasekaran@verinite.com",lessThanNintyDays);

        }
    }



}



