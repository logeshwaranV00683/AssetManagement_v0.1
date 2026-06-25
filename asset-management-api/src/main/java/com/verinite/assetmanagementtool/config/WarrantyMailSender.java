package com.verinite.assetmanagementtool.config;

import com.verinite.assetmanagementtool.entity.AssetsEntity;
import com.verinite.assetmanagementtool.repository.AssetsRepository;
import com.verinite.assetmanagementtool.service.mailservice.NotificationMailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Configuration
public class WarrantyMailSender {

    @Autowired
    private AssetsRepository repository;

    @Autowired
    private NotificationMailer notificationMail;

    @Scheduled(cron = "0 30 11 * * *")
    public void getWarrantyExpiredAssets() throws MessagingException, UnsupportedEncodingException {
        List<AssetsEntity> nearingExpiryAssets = new ArrayList<>();
        List<AssetsEntity> allAssets = repository.findAll();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (AssetsEntity entity : allAssets) {
            LocalDate warrantyDate = entity.getWarrantyDate();

            if (warrantyDate == null) {
                System.out.println("Skipping asset with empty warranty date: " + entity.getAssetName());
                continue;
            }

            try {
                long daysLeft = ChronoUnit.DAYS.between(today, warrantyDate);

                if (daysLeft == 90 || daysLeft == 60 || daysLeft == 30 || (daysLeft <= 15 && daysLeft >= 1)) {
                    System.out.println("Asset '" + entity.getAssetName() + "' warranty expires in " + daysLeft + " days.");
                    nearingExpiryAssets.add(entity);
                }

            } catch (DateTimeParseException e) {
                System.out.println("Invalid warranty date format for asset: " + entity.getAssetName() + " -> " + warrantyDate);
            }
        }

        if (!nearingExpiryAssets.isEmpty()) {
            notificationMail.notifyMailer("logeshwaran.s@verinite.com", nearingExpiryAssets);
        } else {
            System.out.println("No assets with warranty expiring in days.");
        }
    }
}



