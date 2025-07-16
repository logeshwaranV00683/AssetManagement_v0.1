/*
 * package com.verinite.assetmangementtool.controller;
 *
 * import com.verinite.assetmangementtool.entity.AssetTypes; import
 * com.verinite.assetmangementtool.entity.DesignationEntity; import
 * com.verinite.assetmangementtool.service.DashboardService; import
 * com.verinite.assetmangementtool.service.serviceImpl.DesignationServiceImpl; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.web.bind.annotation.*;
 *
 * import java.util.List; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.RequestParam;
 *
 * @RestController
 *
 * @RequestMapping("/assetManager/v1/designation")
 *
 * @CrossOrigin(origins = "http://localhost:4200") public class
 * DesignationController {
 *
 * @Autowired DesignationServiceImpl desiginationService;
 *
 * @Autowired DashboardService dashboardService;
 *
 * // private static final Logger LOGGER = //
 * LoggerFactory.getLogger(AdminController.class);
 *
 * @PostMapping("/addNew") public DesignationEntity newDesignation(@RequestBody
 * DesignationEntity designation) { //
 * LOGGER.info("inside newDesignation method!!!: desigination method", //
 * designation.getPosition(),designation.getTitle()); return
 * desiginationService.newDesigination(designation); }
 *
 * @GetMapping("/getAll") public List<DesignationEntity> getAllDesignation() {
 * // LOGGER.info("inside Get all Designation method!!!: desigination method");
 * return desiginationService.getAll(); }
 *
 * @GetMapping("/getBy/title/{title}") public DesignationEntity
 * getByTitle(@PathVariable String title) { //
 * LOGGER.info("inside Get all Designation method!!!: getByTitle method");
 * return desiginationService.getByTitle(title); }
 *
 * @GetMapping("/getBy/descCode/{code}") public DesignationEntity
 * getByCode(@PathVariable int code) { //
 * LOGGER.info("inside Get all Designation method!!!: getByCode method"); return
 * desiginationService.getByCode(code); }
 *
 * @GetMapping("/getBy/position/{position}") public DesignationEntity
 * getByPosition(@PathVariable String position) { //
 * LOGGER.info("inside Get all Designation method!!!: getByPosition method");
 * return desiginationService.getByPosition(position); }
 *
 * }
 */