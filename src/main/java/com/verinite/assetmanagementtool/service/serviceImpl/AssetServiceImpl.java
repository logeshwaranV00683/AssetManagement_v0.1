package com.verinite.assetmanagementtool.service.serviceImpl;

import com.verinite.assetmanagementtool.dto.AssetExportDto;
import com.verinite.assetmanagementtool.dto.AssetsDto;
import com.verinite.assetmanagementtool.dto.AssignableAssetDto;
import com.verinite.assetmanagementtool.entity.AssetsEntity;
import com.verinite.assetmanagementtool.entity.AssignedAssetsEntity;
import com.verinite.assetmanagementtool.entity.CountOfAssetsEntity;
import com.verinite.assetmanagementtool.repository.*;
import com.verinite.assetmanagementtool.response.SaveAssetResponse;
import com.verinite.assetmanagementtool.service.AssetService;
import com.verinite.assetmanagementtool.service.AssetsHistoryServices;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AssetServiceImpl implements AssetService, ApplicationRunner {


    @Autowired
    AssetsRepository assetRepo;
    @Autowired
    AssetCountRepository assetCountRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    AssignedAssetsRepository assignedAssetsRepository;
    @Autowired
    AssetsHistoryServices assetsHistoryServices;
    @Autowired
    AssignedAssetsServiceImpl assignedAssetsServiceImp;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AdminRegistrationRepository adminRegistrationRepository;
    @Autowired
    AssignedAssetsServiceImpl assignedAssetsService;
    @Autowired
    private Validator validator;
    @Autowired
    private DeletedAssetRepository deletedAssetRepository;


    public ResponseEntity<AssetsDto> saveAsset(AssetsDto assetDto) {
        ModelMapper modelMapper = new ModelMapper();
        AssetsEntity assets = modelMapper.map(assetDto, AssetsEntity.class);

        if (assets.getWarrantyDate().isBefore(assets.getPurchaseDate())) {
            throw new IllegalArgumentException("Warranty Date must not be before Purchase Date");
        }

        if (assetRepo.existsBySerialNumber(assetDto.getSerialNumber())) {
            throw new IllegalArgumentException("Already Serial Number exists");
        }

        if (!adminRegistrationRepository.existsByEmpId(assetDto.getAddedBy())) {
            throw new IllegalArgumentException("Only Admin can add asset");
        }

        assets.setStatus("UnAssigned");

        assetRepo.save(assets);

        String location = assetDto.getLocation();
        String type = assetDto.getType();

        Optional<CountOfAssetsEntity> existing = assetCountRepository
                .findByLocationIgnoreCaseAndTypeIgnoreCase(location, type);

        CountOfAssetsEntity countEntity;
        if (existing.isPresent()) {
            countEntity = existing.get();
            countEntity.setTotal(countEntity.getTotal() + 1);
            countEntity.setUnassigned(countEntity.getUnassigned() + 1);
        } else {
            countEntity = new CountOfAssetsEntity();
            countEntity.setLocation(location);
            String formattedType = type.substring(0, 1).toUpperCase(Locale.ROOT) +
                    type.substring(1).toLowerCase(Locale.ROOT);
            countEntity.setType(formattedType);
            countEntity.setTotal(1);
            countEntity.setAssigned(0);
            countEntity.setUnassigned(1);
            countEntity.setScrapped(0);
        }

        assetCountRepository.save(countEntity);

        return ResponseEntity.ok(modelMapper.map(assets, AssetsDto.class));
    }


    @Override
    public Object getAssetBySerialNumber(String id) {
        try {
            AssetsEntity asset = assetRepo.findBySerialNumber(id);
            if (!asset.getStatus().equalsIgnoreCase("scrap"))
                return asset;
            else {
                return "In Scrap";
            }
        } catch (NoSuchElementException e) {
            return "id Not Found";
        }
    }

    @Override
    public SaveAssetResponse updateAsset(SaveAssetResponse asset) {
        AssetsEntity existingAsset = assetRepo.findBySerialNumber(asset.getSerialNumber());
        if (existingAsset == null) {
            return null;
        }

        String oldStatus = existingAsset.getStatus();
        String oldLocation = existingAsset.getLocation();
        String oldType = existingAsset.getType();

        if (asset.getAssetName() != null)
            existingAsset.setAssetName(asset.getAssetName());
        if (asset.getPurchaseDate() != null)
            if (existingAsset.getWarrantyDate() == null || existingAsset.getWarrantyDate().isAfter(asset.getPurchaseDate())) {
                existingAsset.setPurchaseDate(asset.getPurchaseDate());
            } else {
                throw new IllegalArgumentException("Error: Given Warranty date is Before then the Purchase Date");
            }
        if (asset.getWarrantyDate() != null &&
                (existingAsset.getPurchaseDate() == null || existingAsset.getPurchaseDate().isBefore(asset.getWarrantyDate())))
            existingAsset.setWarrantyDate(asset.getWarrantyDate());
        if (asset.getSerialNumber() != null)
            existingAsset.setSerialNumber(asset.getSerialNumber());
        if (asset.getStatus() != null && !oldStatus.equalsIgnoreCase("Assigned")) {
            if (asset.getStatus().equalsIgnoreCase("Scrap") && asset.getAssignedBy() != null && existingAsset.getAssignedBy() == null && asset.getAssignedDate() != null && existingAsset.getAssignedDate() == null) {
                existingAsset.setAssignedDate(asset.getAssignedDate());
                existingAsset.setAssignedBy(asset.getAssignedBy());
                existingAsset.setStatus("Scrap");
            } else {
                existingAsset.setAssignedDate(null);
                existingAsset.setAssignedBy(null);
                existingAsset.setStatus("Unassigned");
            }
        }
        if (asset.getType() != null)
            existingAsset.setType(asset.getType());
        if (asset.getAddedBy() != null)
            existingAsset.setAddedBy(asset.getAddedBy());
        if (asset.getOperatingSystem() != null)
            existingAsset.setOperatingSystem(asset.getOperatingSystem());
        if (asset.getModelName() != null)
            existingAsset.setModelName(asset.getModelName());
        if (asset.getEmpId() != null && existingAsset.getEmpId() == null)
            existingAsset.setEmpId(asset.getEmpId());
        if (asset.getLocation() != null)
            existingAsset.setLocation(asset.getLocation());
        if (asset.getReturnDate() != null && existingAsset.getReturnDate() == null)
            existingAsset.setReturnDate(asset.getReturnDate());
        if (asset.getAssertSourcedBy() != null)
            existingAsset.setAssetSourcedBy(asset.getAssertSourcedBy());

        boolean isLocationChanged = !oldLocation.equalsIgnoreCase(existingAsset.getLocation());
        boolean isTypeChanged = !oldType.equalsIgnoreCase(existingAsset.getType());
        boolean isStatusChanged = !oldStatus.equalsIgnoreCase(existingAsset.getStatus());

        if (isLocationChanged || isTypeChanged || isStatusChanged) {
            updateCountTable(oldLocation, oldType, oldStatus, -1);
            updateCountTable(existingAsset.getLocation(), existingAsset.getType(), existingAsset.getStatus(), 1);
        }

        AssetsEntity saved = assetRepo.save(existingAsset);
        return modelMapper.map(saved, SaveAssetResponse.class);
    }

    private void updateCountTable(String location, String type, String status, int delta) {
        Optional<CountOfAssetsEntity> optionalCount = assetCountRepository.findByLocationIgnoreCaseAndTypeIgnoreCase(location, type);

        CountOfAssetsEntity count = optionalCount.orElseGet(() -> {
            CountOfAssetsEntity newCount = new CountOfAssetsEntity();
            newCount.setLocation(location.trim());
            newCount.setType(type.trim());
            newCount.setTotal(0);
            newCount.setAssigned(0);
            newCount.setUnassigned(0);
            newCount.setScrapped(0);
            return newCount;
        });

        count.setTotal(count.getTotal() + delta);

        switch (status.toLowerCase()) {
            case "assigned":
                count.setAssigned(count.getAssigned() + delta);
                break;
            case "unassigned":
                count.setUnassigned(count.getUnassigned() + delta);
                break;
            case "scrap":
                count.setScrapped(count.getScrapped() + delta);
                break;
            default:
                break;
        }
        assetCountRepository.save(count);
    }

    @Override
    public void scrapAsset(int id) {
        Optional<AssetsEntity> optionalAsset = assetRepo.findById(id);
        if (optionalAsset.isEmpty()) {
            throw new IllegalArgumentException("Invalid asset ID: " + id);
        }

        AssetsEntity asset = optionalAsset.get();
        if (!"unassigned".equalsIgnoreCase(asset.getStatus())) {
            throw new IllegalArgumentException("Asset must be proper Status before Scrapping.");
        }
        asset.setStatus("Scrap");
        assetRepo.save(asset);

        String location = asset.getLocation().trim();
        String assetType = asset.getType().trim();

        Optional<CountOfAssetsEntity> optionalEntity =
                assetCountRepository.findByLocationIgnoreCaseAndTypeIgnoreCase(location, assetType);

        CountOfAssetsEntity entity = optionalEntity.orElseGet(() -> {
            CountOfAssetsEntity newEntity = new CountOfAssetsEntity();
            newEntity.setLocation(location);
            newEntity.setType(assetType);
            newEntity.setTotal(0);
            newEntity.setAssigned(0);
            newEntity.setUnassigned(0);
            newEntity.setScrapped(0);
            return newEntity;
        });
        entity.setUnassigned((entity.getUnassigned() != null ? entity.getUnassigned() : 0) - 1);
        entity.setScrapped((entity.getScrapped() != null ? entity.getScrapped() : 0) + 1);

        assetCountRepository.save(entity);
    }

    @Override
    public List<AssetsEntity> getThroughStatus(String str) {
        List<AssetsEntity> assets = get();
        List<AssetsEntity> assets2 = new ArrayList<>();
        for (AssetsEntity i : assets) {
            if (i.getStatus().equalsIgnoreCase(str))
                assets2.add(i);
        }
        return assets2;
    }

    public List<AssetsEntity> get() {
        return assetRepo.findAll();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    @Override
    public int getCountOfAssigned() {
        Integer totalAssigned = assetCountRepository.getTotalAssigned();
        return (totalAssigned != null) ? totalAssigned : 0;
    }

    @Override
    public int getCountOfUnassigned() {
        Integer totalUnassigned = assetCountRepository.getTotalUnassigned();
        return (totalUnassigned != null) ? totalUnassigned : 0;
    }

    @Override
    public List<AssetsEntity> getUnAssigned() {
        return assetRepo.findAll().stream().filter(x -> x.getStatus().equalsIgnoreCase("unassigned"))
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetsEntity> getAssigned() {
        return assetRepo.findAll().stream().filter(x -> x.getStatus().equalsIgnoreCase("assigned"))
                .collect(Collectors.toList());
    }

    @Override
    public AssetsDto getAssetsDetails(Integer assetId) {
        if (assetId == null) {
            throw new RuntimeException("Asset id is empty: null");
        }

        Optional<AssetsEntity> optionalAssetsEntity = assetRepo.findByAssetId(assetId);

        if (optionalAssetsEntity.isEmpty()) {
            throw new RuntimeException("No asset found with id: " + assetId);
        }

        AssetsEntity assetsEntity = optionalAssetsEntity.get();
        return modelMapper.map(assetsEntity, AssetsDto.class);
    }

    @Override
    public List<AssetsDto> listOfAllAsset() {
        List<AssetsEntity> assetsEntities = assetRepo.findAllByOrderByAssetIdDesc(); // Fetch all asset entities
        return assetsEntities.stream().map(asset -> modelMapper.map(asset, AssetsDto.class)) // Convert each entity to
                // DTO
                .collect(Collectors.toList()); // Collect the results as a list of DTOs
    }

    @Override
    public void exportAssetsToExcel(List<AssetExportDto> allAssets, OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        List<AssetExportDto> assigned = allAssets.stream()
                .filter(a -> "Assigned".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());
        writeAssignedSheet(workbook, assigned, headerStyle, dataStyle);

        List<AssetExportDto> unassigned = allAssets.stream()
                .filter(a -> "Unassigned".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());
        writeUnassignedSheet(workbook, unassigned, headerStyle, dataStyle);

        List<AssetExportDto> scrap = allAssets.stream()
                .filter(a -> "Scrap".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());
        writeScrapSheet(workbook, scrap, headerStyle, dataStyle);

        workbook.write(outputStream);
        workbook.close();
    }

    private void writeAssignedSheet(Workbook workbook, List<AssetExportDto> data, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Assigned Assets");
        String[] headers = {
                "Asset Name", "Serial Number", "Assigned To (Emp ID)", "Status", "Type", "Purchase Date",
                "Warranty Date", "Location", "Model Name", "Operating System",
                /*"Return Date",*/ "Added By", "Assigned Date", "Assigned By", "Sourced By"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (AssetExportDto dto : data) {
            Row row = sheet.createRow(rowNum++);
            int col = 0;
            createDataCell(row, col++, dto.getAssetName(), dataStyle);
            createDataCell(row, col++, dto.getSerialNumber(), dataStyle);
            createDataCell(row, col++, dto.getAssignedTo(), dataStyle);
            createDataCell(row, col++, dto.getStatus(), dataStyle);
            createDataCell(row, col++, dto.getType(), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getPurchaseDate()), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getWarrantyDate()), dataStyle);
            createDataCell(row, col++, dto.getLocation(), dataStyle);
            createDataCell(row, col++, dto.getModelName(), dataStyle);
            createDataCell(row, col++, dto.getOperatingSystem(), dataStyle);
//            createDataCell(row, col++, String.valueOf(dto.getReturnDate()), dataStyle);
            createDataCell(row, col++, dto.getAddedBy(), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getAssignedDate()), dataStyle);
            createDataCell(row, col++, dto.getAssignedBy(), dataStyle);
            createDataCell(row, col, dto.getAssetSourcedBy(), dataStyle);
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
    }

    private void writeUnassignedSheet(Workbook workbook, List<AssetExportDto> data, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Unassigned Assets");
        String[] headers = {
                "Asset Name", "Serial Number", "Status", "Type", "Purchase Date",
                "Warranty Date", "Location", "Model Name", "Operating System",
                "Added By", "Sourced By"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (AssetExportDto dto : data) {
            Row row = sheet.createRow(rowNum++);
            int col = 0;
            createDataCell(row, col++, dto.getAssetName(), dataStyle);
            createDataCell(row, col++, dto.getSerialNumber(), dataStyle);
            createDataCell(row, col++, dto.getStatus(), dataStyle);
            createDataCell(row, col++, dto.getType(), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getPurchaseDate()), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getWarrantyDate()), dataStyle);
            createDataCell(row, col++, dto.getLocation(), dataStyle);
            createDataCell(row, col++, dto.getModelName(), dataStyle);
            createDataCell(row, col++, dto.getOperatingSystem(), dataStyle);
            createDataCell(row, col++, dto.getAddedBy(), dataStyle);
            createDataCell(row, col, dto.getAssetSourcedBy(), dataStyle);
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
    }

    private void writeScrapSheet(Workbook workbook, List<AssetExportDto> data, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Scraped Assets");
        String[] headers = {
                "Asset Name", "Serial Number", "Purchase Date", "Scraped Date", "Scraped By",
                "Operating System"/*, "Users"*/, "Status", "Type", "Location",
                "Model Name", "Added By", "Sourced By"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (AssetExportDto dto : data) {
            Row row = sheet.createRow(rowNum++);
            int col = 0;
            createDataCell(row, col++, dto.getAssetName(), dataStyle);
            createDataCell(row, col++, dto.getSerialNumber(), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getPurchaseDate()), dataStyle);
            createDataCell(row, col++, String.valueOf(dto.getAssignedDate()), dataStyle);
            createDataCell(row, col++, dto.getAssignedBy(), dataStyle);
            createDataCell(row, col++, dto.getOperatingSystem(), dataStyle);
            //    createDataCell(row, col++, dto.EmpId(), dataStyle); // Users
            createDataCell(row, col++, dto.getStatus(), dataStyle);
            createDataCell(row, col++, dto.getType(), dataStyle);
            createDataCell(row, col++, dto.getLocation(), dataStyle);
            createDataCell(row, col++, dto.getModelName(), dataStyle);
            createDataCell(row, col++, dto.getAddedBy(), dataStyle);
            createDataCell(row, col, dto.getAssetSourcedBy(), dataStyle);
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
    }

    private void createDataCell(Row row, int colIdx, String value, CellStyle style) {
        Cell cell = row.createCell(colIdx);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    @Override
    public ResponseEntity<?> importAssetsFromExcel(InputStream inputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Map<String, String> data = new HashMap<>();

            Map<String, String> unassigned = importUnassignedAssets(workbook.getSheet("Unassigned Assets"));
            Map<String, String> assigned = importAssignedAssets(workbook.getSheet("Assigned Assets"));

            if (unassigned != null) {
                unassigned.entrySet().stream()
                        .filter(e -> e.getKey() != null && e.getValue() != null)
                        .forEach(e -> data.put(e.getKey(), e.getValue()));
            }

            if (assigned != null) {
                assigned.entrySet().stream()
                        .filter(e -> e.getKey() != null && e.getValue() != null)
                        .forEach(e -> data.put(e.getKey(), e.getValue()));
            }
            Map<String, String> scrapedAssets = importScrapedAssets(workbook.getSheet("Scraped Assets"));
            if (scrapedAssets != null) {
                scrapedAssets.entrySet().stream()
                        .filter(e -> e.getKey() != null && e.getValue() != null)
                        .forEach(e -> data.put(e.getKey(), e.getValue()));
            }
            return data.isEmpty() ? ResponseEntity.ok("All Data Inserted Successfully into Data Base") : ResponseEntity.ok(data);
        }
    }

    private Map<String, String> importUnassignedAssets(Sheet sheet) {
        if (sheet == null) {
            log.warn("Sheet 'Unassigned Assets' not found.");
            return null;
        }

        Iterator<Row> rows = sheet.iterator();
        if (rows.hasNext()) rows.next();
        Map<String, String> skippedData = new HashMap<>();
        while (rows.hasNext()) {
            Row row = rows.next();
            AssetsDto asset = new AssetsDto();
            asset.setAssetName((getCellValue(row, 0)));
            asset.setSerialNumber(getCellValue(row, 1));
            asset.setStatus("Unassigned");
            asset.setType(getCellValue(row, 3));
            asset.setPurchaseDate(parseDateSafe(getCellValue(row, 4)));
            asset.setWarrantyDate(parseDateSafe(getCellValue(row, 5)));
            asset.setLocation(getCellValue(row, 6));
            asset.setModelName(getCellValue(row, 7));
            asset.setOperatingSystem(getCellValue(row, 8));
            asset.setAddedBy(getCellValue(row, 9));
            asset.setAssetSourcedBy(getCellValue(row, 10));
            if (assetRepo.existsBySerialNumber(asset.getSerialNumber())) {
                log.warn("Skipping row due to already exist serial number while importing Unassigned Asset.");
                skippedData.put(asset.getSerialNumber(), "Skipping row due to already exist serial number while importing Unassigned Asset");
                continue;
            }
            Set<ConstraintViolation<AssetsDto>> violations = validator.validate(asset);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<AssetsDto> v : violations) {
                    sb.append(v.getPropertyPath())
                            .append(": ")
                            .append(v.getMessage())
                            .append("; ");
                }
                skippedData.put(asset.getSerialNumber(), "Validation Failed: " + sb);
                continue;
            }
            try {
                saveAsset(asset);
            } catch (IllegalArgumentException e) {
                skippedData.put(asset.getSerialNumber(), e.getMessage());
            }
        }
        return skippedData;
    }

    private Map<String, String> importAssignedAssets(Sheet sheet) {
        if (sheet == null) {
            log.warn("Sheet 'Assigned Assets' not found.");
            return null;
        }


        Iterator<Row> rows = sheet.iterator();
        if (rows.hasNext()) rows.next();
        Map<String, String> skippedData = new HashMap<>();
        while (rows.hasNext()) {
            Row row = rows.next();
            AssetsDto asset = new AssetsDto();
            asset.setEmpId(getCellValue(row, 2));
            asset.setSerialNumber(getCellValue(row, 1));
            if (asset.getEmpId() != null && employeeRepository.existsById(asset.getEmpId())) {
                System.out.println(asset.getEmpId());
                asset.setAssetName(getCellValue(row, 0));
                asset.setStatus("Assigned");
                asset.setType(getCellValue(row, 4));
                asset.setPurchaseDate(parseDateSafe(getCellValue(row, 5)));
                asset.setWarrantyDate(parseDateSafe(getCellValue(row, 6)));
                asset.setLocation(getCellValue(row, 7));
                asset.setModelName(getCellValue(row, 8));
                asset.setOperatingSystem(getCellValue(row, 9));
                asset.setAddedBy(getCellValue(row, 10));
                asset.setAssignedBy(getCellValue(row, 12));
                asset.setAssetSourcedBy(getCellValue(row, 13));
                asset.setAssignedDate(parseDateSafe(getCellValue(row, 11)));
                Set<ConstraintViolation<AssetsDto>> violations = validator.validate(asset);
                if (!violations.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (ConstraintViolation<AssetsDto> v : violations) {
                        sb.append(v.getPropertyPath())
                                .append(": ")
                                .append(v.getMessage())
                                .append("; ");
                    }
                    skippedData.put(asset.getSerialNumber(), "Validation Failed: " + sb);
                    continue;
                }

                if (!assetRepo.existsBySerialNumber(asset.getSerialNumber())) {
                    try {
                        asset = saveAsset(asset).getBody();
                    } catch (IllegalArgumentException e) {
                        skippedData.put(asset.getSerialNumber(), e.getMessage());
                        continue;
                    }
                } else {
                    log.warn("Skipping save the row due to already exist serial number while Importing Assigned Asset.");

                    AssetsEntity assetsEntity = assetRepo.findBySerialNumber(asset.getSerialNumber());
                    if (assetsEntity.getAssetName().equalsIgnoreCase(asset.getAssetName()) && assetsEntity.getPurchaseDate().isEqual(asset.getPurchaseDate())) {
                        if (asset.getStatus().equalsIgnoreCase("unassigned")) {
                            assetsEntity.setAssignedBy(asset.getAssignedBy());
                            assetsEntity.setAssignedDate(asset.getAssignedDate());
                            assetsEntity.setEmpId(asset.getEmpId());
                            asset = modelMapper.map(assetsEntity, AssetsDto.class);
                        } else {
                            log.warn("Skipping the Asset Assigning Because Asset Already Present and it status is {} [Serial Number: {}]", asset.getStatus(), asset.getSerialNumber());
                            skippedData.put(asset.getSerialNumber(), "Skipping the Asset Assigning Because Asset Already Present and it status is " + asset.getStatus());
                            continue;
                        }
                    } else {
                        log.warn("Skipping Asset Assigning due to already exist serial number while Importing Assigned Asset, Importing asset differ from the DB asset [Serial Number: {}]", asset.getSerialNumber());
                        skippedData.put(asset.getSerialNumber(), "Skipping Asset Assigning due to already exist serial number while Importing Assigned Asset, Importing asset differ from the data base asset ");
                        continue;
                    }
                }
                asset.setStatus("Assigned");
                assetRepo.save(modelMapper.map(asset, AssetsEntity.class));
                AssignableAssetDto assignableAssetDto = new AssignableAssetDto();
                assignableAssetDto.setAssignedBy(asset.getAssignedBy());
                assignableAssetDto.setAssignedDate(asset.getAssignedDate());
                AssignedAssetsEntity assignedAssetsEntity = assignedAssetsServiceImp.getAssignedAssetsEntity(assignableAssetDto
                        , modelMapper.map(asset, AssetsEntity.class));
                assignedAssetsService.assignedCountImport((Objects.requireNonNull(getCellValue(row, 4))), (Objects.requireNonNull(getCellValue(row, 7))));
                assignedAssetsRepository.save(assignedAssetsEntity);
                assetsHistoryServices.saveHistory(assignedAssetsEntity);
            } else {
                log.warn("No employee found for EmpId: {}", asset.getEmpId());
                skippedData.put(asset.getSerialNumber(), "Skipping the Row due to No employee found for EmpId: " + asset.getEmpId() + " while Importing Assigned Asset");
            }
        }
        return skippedData;
    }

    private Map<String, String> importScrapedAssets(Sheet sheet) {
        if (sheet == null) {
            log.warn("Sheet 'Scraped Assets' not found.");
            return null;
        }

        Iterator<Row> rows = sheet.iterator();
        if (rows.hasNext()) rows.next();
        Map<String, String> skippedData = new HashMap<>();
        while (rows.hasNext()) {
            Row row = rows.next();
            AssetsDto asset = new AssetsDto();
            asset.setAssetName(getCellValue(row, 0));
            asset.setSerialNumber(getCellValue(row, 1));
            asset.setPurchaseDate(parseDateSafe(getCellValue(row, 2)));
            asset.setWarrantyDate(asset.getPurchaseDate());

            if (asset.getSerialNumber() == null || asset.getSerialNumber().isEmpty()) {
                log.warn("Skipping row due to missing serial number while importing Scrap Asset.");
                continue;
            }
            AssetsEntity assetsEntity = assetRepo.findBySerialNumber(asset.getSerialNumber());
            if (assetsEntity != null) {
                if (assetsEntity.getAssetName().equalsIgnoreCase(asset.getAssetName()) && assetsEntity.getPurchaseDate().isEqual(asset.getPurchaseDate())) {
                    try {
                        scrapAsset(assetsEntity.getAssetId());
                    } catch (IllegalArgumentException e) {
                        skippedData.put(asset.getSerialNumber(), e.getMessage());
                        continue;
                    }
                } else {
                    skippedData.put(asset.getSerialNumber(), "Skipping row due to AssetName and PurchaseDate are not matched with the on in the Data Base while Scapping Asset");
                }
                continue;
            }
            asset.setAssignedDate(parseDateSafe(getCellValue(row, 3)));
            asset.setAssignedBy(getCellValue(row, 4));
            asset.setOperatingSystem(getCellValue(row, 5));
            asset.setStatus(getCellValue(row, 6));
            asset.setType(getCellValue(row, 7));
            asset.setLocation(getCellValue(row, 8));
            asset.setModelName(getCellValue(row, 9));
            asset.setAddedBy(getCellValue(row, 10));
            asset.setAssetSourcedBy(getCellValue(row, 11));
            Set<ConstraintViolation<AssetsDto>> violations = validator.validate(asset);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<AssetsDto> v : violations) {
                    sb.append(v.getPropertyPath())
                            .append(": ")
                            .append(v.getMessage())
                            .append("; ");
                }
                skippedData.put(asset.getSerialNumber(), "Validation Failed: " + sb);
                continue;
            }
            try {
                scrapAsset(Objects.requireNonNull(saveAsset(asset).getBody()).getAssetId());
            } catch (IllegalArgumentException e) {
                skippedData.put(asset.getSerialNumber(), e.getMessage());
            }
        }
        return skippedData;
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }


    private LocalDate parseDateSafe(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;

        try {
            Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            return parsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e) {
            try {
                double excelDate = Double.parseDouble(dateStr);
                Date parsed = DateUtil.getJavaDate(excelDate);
                return parsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (NumberFormatException ne) {
                log.warn("Invalid date: {}", dateStr);
                return null;
            }
        }
    }

    public List<String> getUniqueAssetSourcedBy() {
        return assetRepo.getUniqueAssetSourcedBy();
    }
}