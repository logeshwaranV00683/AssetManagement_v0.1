/*
 * package com.verinite.assetmangementtool.controller;
 * 
 * import com.verinite.assetmangementtool.entity.LocationEntity; import
 * com.verinite.assetmangementtool.service.LocationService; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.boot.ApplicationArguments; import
 * org.springframework.boot.ApplicationRunner; import
 * org.springframework.http.HttpStatus; import
 * org.springframework.validation.FieldError; import
 * org.springframework.web.bind.MethodArgumentNotValidException; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import javax.validation.Valid; import java.util.HashMap; import
 * java.util.List; import java.util.Map;
 * 
 * @RestController
 * 
 * @RequestMapping("/assetManager/v1/")
 * 
 * @CrossOrigin(origins = "http://localhost:4200") public class
 * LocationController implements ApplicationRunner {
 * 
 * // private static final Logger LOGGER = //
 * LoggerFactory.getLogger(LocationController.class);
 * 
 * @Autowired LocationService locationService;
 * 
 * @PostMapping("location/save") public LocationEntity
 * saveEntity(@Valid @RequestBody LocationEntity location) { //
 * LOGGER.debug("Hitted Location save endPoint"); return
 * locationService.saveLocation(location); }
 * 
 * @GetMapping("locatoin/get/id/{id}") public Object getById(@PathVariable int
 * id) { // LOGGER.debug("Hited getLocationById endPoint "); return
 * locationService.getLocatioById(id); }
 * 
 * @GetMapping("locaion/get/state/{state}") public List<Object>
 * getByState(@PathVariable String state) { //
 * LOGGER.debug("Hited getStateById endPoint"); return
 * locationService.getByStateName(state); }
 * 
 * @GetMapping("location/get/country/{country}") public List<Object>
 * getByCountrt(@PathVariable String country) { //
 * LOGGER.debug("Hited getByCountry endPoint"); return
 * locationService.getByCountry(country); }
 * 
 * @PatchMapping("location/update/by/id/{id}") public Object
 * update(@PathVariable int id, @RequestBody LocationEntity location) { //
 * LOGGER.debug("Hited updateLocation endPoint"); return
 * locationService.updateLocation(id, location); }
 * 
 * @GetMapping("location/get/all") public List<LocationEntity> getAll() { //
 * LOGGER.debug("Hited getAllLocations endPoint"); return
 * locationService.getAllLocations(); }
 * 
 * @ResponseStatus(HttpStatus.BAD_REQUEST)
 * 
 * @ExceptionHandler(MethodArgumentNotValidException.class) public Map<String,
 * String> handleValidationExceptions(MethodArgumentNotValidException ex) {
 * Map<String, String> errors = new HashMap<>();
 * ex.getBindingResult().getAllErrors().forEach((error) -> { String fieldName =
 * ((FieldError) error).getField(); String errorMessage =
 * error.getDefaultMessage(); errors.put(fieldName, errorMessage); }); return
 * errors; }
 * 
 * @Override public void run(ApplicationArguments args) throws Exception {
 * 
 * } }
 */