package com.example.luxuryhotel.contrloller;

import com.example.luxuryhotel.entities.Apartment;
import com.example.luxuryhotel.entities.Clazz;
import com.example.luxuryhotel.model.repository.ApartmentRepository;
import com.example.luxuryhotel.model.service.ApartmentManager;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RestController
@RequestMapping("/api")
public class HotelApiController {
    @Autowired
    ApartmentRepository apartmentRepo;
    @Autowired
    ApartmentManager apartmentManager;

    @GetMapping("/apartmentRatio")
    public List<Object> apartmentRatio(HttpServletRequest request, Model model) {
        List<Object> ratio = new ArrayList<>();
        ratio.add(List.of("Apartments class", "countity"));
        ratio.addAll(apartmentRepo.countGroupedByClazzApartment());
        return ratio;
    }

    @GetMapping("/apartmentOccupancy")
    public List<Object> apartmentOccupancy(HttpServletRequest request, Model model) {
        List<Object> ratio = new ArrayList<>();
        ratio.add(List.of("Місяць", "Кількість доступних"));
        for (int i = 1; i <= 12; i++){
            ratio.add(List.of(Month.of(i).name(),apartmentManager.getThisYearAvailableApartments(Month.of(i)).size()));
        }
        return ratio;
    }

    @GetMapping("/apartmentsExcel")
    public ResponseEntity<Resource> getApartmentsExcel(HttpServletRequest request, Model model) throws IOException {
        //response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=apartment" + LocalDate.now() + ".xlsx";
        //response.setHeader(headerKey, headerValue);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(new ByteArrayResource(apartmentManager.generateExcelFile(apartmentRepo.findAll()).toByteArray() ));
    }


    @PostMapping(value = "/apartmentsPostExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integer> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        List<Apartment> apartments = new ArrayList<>();
        try {
            InputStream is = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);



            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    continue;
                }

                Apartment user = new Apartment();
                user.setPrice((int) row.getCell(0).getNumericCellValue());
                user.setBeds((int) row.getCell(1).getNumericCellValue());
                user.setClazz(Clazz.valueOf(row.getCell(2).getStringCellValue()));
                apartments.add(user);
            }
            is.close();
            workbook.close();

            apartmentRepo.saveAll(apartments);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("error here");
        }
        // Return the parsed data as a response body
        return new ResponseEntity<>(apartments.size(), HttpStatus.OK);
    }



}
