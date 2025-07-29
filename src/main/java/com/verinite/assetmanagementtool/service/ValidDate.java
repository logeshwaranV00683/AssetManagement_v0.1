//package com.verinite.assetmanagementtool.service;
//
//import com.verinite.assetmanagementtool.service.serviceImpl.ValidDateValidator;
//
//import javax.validation.Constraint;
//import javax.validation.Payload;
//import java.lang.annotation.*;
//
//@Documented
//@Constraint(validatedBy = ValidDateValidator.class)
//@Target({ElementType.FIELD})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ValidDate {
//    String message() default "Invalid date";
//    Class<?>[] groups() default {};
//    Class<? extends Payload>[] payload() default {};
//}