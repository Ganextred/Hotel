package com.example.luxuryhotel.model.service;


import com.example.luxuryhotel.entities.*;
import com.example.luxuryhotel.model.repository.ApartmentRepository;
import com.example.luxuryhotel.model.repository.ApartmentStatusRepository;
import com.example.luxuryhotel.model.repository.RequestRepository;
import com.example.luxuryhotel.model.repository.UserRepository;
import com.sun.mail.iap.ByteArray;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class ApartmentManager {
    @Autowired
    ApartmentRepository apartmentRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    ApartmentStatusRepository apartmentStatusRepo;
    @Autowired
    RequestRepository requestRepo;
    @Autowired
    Validator valid;
    @Autowired
    CookieManager cookieManager;

    private final static Integer pageCapacity = 9;
    private final static Logger logger = Logger.getLogger(ApartmentManager.class);

    public List<Apartment> getDefaultApartments(String arrivalDay, String endDay, Integer page){
        Map<String,Boolean> m = new HashMap<>();
        m.put ("AVAILABLE", true);
        return getSortedApartments(arrivalDay, endDay, new String[]{"price"}, new Boolean[]{true}, m, page);
    }

    public void addDefaultModelSortParams(Model model, String arrivalDay, String endDay, Integer page){
        model.addAttribute("page", page);
        TimeInterval timeInterval = TimeInterval.getValidInterval(arrivalDay, endDay);
        List<Apartment> apartments= getDefaultApartments(arrivalDay, endDay, page);
        model.addAttribute("apartments", apartments);
        model.addAttribute("arrivalDayD", timeInterval.arrivalDay);
        model.addAttribute("endDayD", timeInterval.endDay);
        model.addAttribute("sortParamsD", new String[]{"price", "price", "price"});
        model.addAttribute("orderParamsD", new Boolean[]{true, true, true});
        Map<String,Boolean> m = new HashMap<>();
        m.put ("AVAILABLE", true);
        model.addAttribute("statusD", m);
    }

    public void addModelFlashSortParams(RedirectAttributes rA, String arrivalDay, String endDay, String[] sortParams, Boolean[] orderParams, Map<String, Boolean> status, Integer page){
        rA.addFlashAttribute("page", page);
        TimeInterval timeInterval = TimeInterval.getValidInterval(arrivalDay, endDay);
        List<Apartment> apartments= getSortedApartments(arrivalDay, endDay, sortParams, orderParams, status, page);
        rA.addFlashAttribute("apartments", apartments);
        rA.addFlashAttribute("arrivalDayD", timeInterval.arrivalDay);
        rA.addFlashAttribute("endDayD", timeInterval.endDay);
        rA.addFlashAttribute("sortParamsD", sortParams);
        rA.addFlashAttribute("orderParamsD", orderParams);
        rA.addFlashAttribute("statusD", status);
    }


    public List<Apartment> getSortedApartments(String arrivalDay, String endDay, String[] sortParams, Boolean[] orderParams, Map<String, Boolean> status, Integer page){
        Sort sortBySortParams = getSort(sortParams, orderParams);
        Pageable pageable = PageRequest.of(page-1,pageCapacity,sortBySortParams);
        TimeInterval timeInterval = TimeInterval.getValidInterval(arrivalDay, endDay);
        return apartmentRepo.
                findWthStatus(timeInterval.arrivalDay, timeInterval.endDay,
                                status.containsKey("AVAILABLE"), status.containsKey("BOOKED"),
                                status.containsKey("BOUGHT"), status.containsKey("INACCESSIBLE"),
                                pageable);
    }

    public List<Apartment> getThisYearAvailableApartments(Month month){
        int year = LocalDate.now().getYear();
        return apartmentRepo.
                findWthStatus(LocalDate.of(year,month,1), LocalDate.of(year,month, month.length(Year.of(year).isLeap())),
                        true, false,
                        false, false,
                        Pageable.unpaged());
    }


    public Sort getSort (String[] sortParams, Boolean[] orderParams){
        if (sortParams == null || sortParams.length == 0)
            sortParams = new String[]{"price"};
        if (orderParams == null || orderParams.length == 0)
            orderParams = new Boolean[]{true};
        if (orderParams.length != sortParams.length){
            sortParams = new String[]{"price"};
            orderParams = new Boolean[]{true};
        }
        Sort sortBySortParams = orderParams[0]?Sort.by(Sort.Direction.ASC, sortParams[0]):Sort.by(Sort.Direction.DESC, sortParams[0]);
        for (int i = 1; i<orderParams.length; i++)
            if (orderParams[i])
                sortBySortParams = sortBySortParams.and(Sort.by(Sort.Direction.ASC, sortParams[i]));
            else sortBySortParams = sortBySortParams.and(Sort.by(Sort.Direction.DESC, sortParams[i]));
        return sortBySortParams;
    }

    @Transactional
    public Pair<List<String>, ApartmentStatus> book(String arrivalDay, String endDay, User user, Apartment apartment, boolean forRequest) {
        Status bookTypeStatus = forRequest?Status.BOOKEDREQUEST:Status.BOOKED;
        List<String> status = valid.bookApartment(arrivalDay,endDay,apartment);
        ApartmentStatus apartmentStatus = new ApartmentStatus();
        if (status.size()==0){
            apartmentStatus =
                    new ApartmentStatus(apartment,user,LocalDate.parse(arrivalDay),LocalDate.parse(endDay),
                           LocalDateTime.now().plusDays(2), bookTypeStatus);
            apartmentStatusRepo.save(apartmentStatus);
            sendBill(user.getEmail()+"dfsfs",apartment.getPrice());
        }
        return Pair.of(status, apartmentStatus);
    }


    @Transactional
    public List<String> confirmBook(ApartmentStatus apartmentStatus) {
        List<String> status = new ArrayList<>();
        apartmentStatus.setStatus(Status.BOUGHT);
        apartmentStatus.setPayTimeLimit(null);
        apartmentStatusRepo.save(apartmentStatus);
        return status;
    }

    @Transactional
    public List<String> confirmRequest(Request request) {
        List<String> status = new ArrayList<>();
        request.getAnswerStatus().setStatus(Status.BOOKED);
        request.getAnswerStatus().setPayTimeLimit(LocalDateTime.now().plusDays(2));
        apartmentStatusRepo.save(request.getAnswerStatus());
        requestRepo.delete(request);
        return status;
    }

    @Transactional
    public List<String> answerRequest(Request request, Apartment apartment) {
        Pair<List<String>,ApartmentStatus> bookResult = book(request.getArrivalDay().toString(), request.getEndDay().toString(), request.getUserId(), apartment, true);
        List<String> status = bookResult.getFirst();
        ApartmentStatus apartmentStatus = bookResult.getSecond();
        if (status.size()==0){
                request.setAnswerStatus(apartmentStatus);
                requestRepo.save(request);
        }
        return status;
    }

    @Transactional
    public List<String> updateApartment(Apartment apartment, Integer price, Clazz clazz, Integer beds, MultipartFile file) {
        List<String> status = valid.updateApartment(price,clazz,beds);
        if (status.size()==0){
            try {
                if (!file.isEmpty()){
                    file.transferTo(new File(new File("").getAbsolutePath()+ "/src/main/upload/room-"
                            +apartment.getId()+'-'+apartment.getImages().size()));
                    apartment.getImages().add("room-" + apartment.getId().toString()+'-'+apartment.getImages().size());
                }
                apartment.setBeds(beds);
                apartment.setClazz(clazz);
                apartment.setPrice(price);
                apartmentRepo.save(apartment);
            } catch (IOException | IllegalStateException e) {
                status.add("problemsWithFile");
            }
        }
        return status;
    }

    @Transactional
    public Apartment newApartment(){
        List<String> messages = new ArrayList<>();
        Apartment apartment = new Apartment();
        apartment.setPrice(9999).setBeds(1).setClazz(Clazz.LUX);
        return apartmentRepo.save(apartment);
    }

    @Transactional
    public void deleteApartment(Apartment apartment) {
        apartmentRepo.delete(apartment);
        List<String> images = apartment.getImages();
        for (String image: images) {
            File file = new File((new File("").getAbsolutePath() + "/src/main/resources/upload/" + image));
            if (!file.delete())
                logger.warn("Room image wasn`t found in file system");
        }
    }

    public void sendBill(String to, Integer price){}


    public ByteArrayOutputStream generateExcelFile(List<Apartment> apartmentList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet;


        sheet = workbook.createSheet("Student");
        Row row = sheet.createRow(0);
        createCell(row, 0, "ID", sheet);
        createCell(row, 1, "PRICE", sheet);
        createCell(row, 2, "BEDS", sheet);
        createCell(row, 3, "Class", sheet);

        int rowCount = 1;
        for (Apartment apartment: apartmentList) {
            Row nextRow = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(nextRow, columnCount++, apartment.getId(), sheet);
            createCell(nextRow, columnCount++, apartment.getPrice(), sheet);
            createCell(nextRow, columnCount++, apartment.getBeds(), sheet);
            createCell(nextRow, columnCount++, apartment.getClazz().toString(), sheet);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        return outputStream;
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, XSSFSheet sheet) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
    }


    public static class TimeInterval{
        private LocalDate arrivalDay;
        private LocalDate endDay;

        private TimeInterval(String arrivalDayStr, String endDayStr) {
            this.arrivalDay = LocalDate.parse(arrivalDayStr);
            this.endDay = LocalDate.parse(endDayStr);
        }

        public LocalDate getArrivalDay() {
            return arrivalDay;
        }

        public void setArrivalDay(LocalDate arrivalDay) {
            TimeInterval ti = TimeInterval.getValidInterval(arrivalDay.toString(), endDay.toString());
            this.arrivalDay = ti.getArrivalDay();
            this.endDay = (ti.getEndDay());
        }

        public LocalDate getEndDay() {
            return endDay;
        }

        public void setEndDay(LocalDate endDay) {
            TimeInterval ti = TimeInterval.getValidInterval(arrivalDay.toString(), endDay.toString());
            this.arrivalDay = ti.getArrivalDay();
            this.endDay = (ti.getEndDay());
        }

        public static TimeInterval getValidInterval(String arrivalDayStr, String endDayStr){
            if (arrivalDayStr == null || arrivalDayStr.length() == 0){
                arrivalDayStr = LocalDate.now().toString();
                logger.warn("ApartmentManager.makeDateValid got null arrival day");
            }
            if (endDayStr == null || endDayStr.length() == 0){
                endDayStr = LocalDate.now().toString();
                logger.warn("ApartmentManager.makeDateValid got null end day");
            }
            try{
                LocalDate arrivalDateT = LocalDate.parse(arrivalDayStr);
                LocalDate endDateT = LocalDate.parse(endDayStr);
                if (arrivalDateT.compareTo(LocalDate.now()) < 0)
                    arrivalDayStr = LocalDate.now().toString();
                if (endDateT.compareTo(LocalDate.now()) < 0)
                    endDayStr = LocalDate.now().toString();
                if (arrivalDateT.compareTo(endDateT)>0){
                    arrivalDayStr = endDateT.toString();
                    endDayStr = arrivalDateT.toString();
                }
            }catch (DateTimeParseException e){
                logger.warn("ApartmentManager.makeDateValid got unparsed date", e);
                arrivalDayStr = LocalDate.now().toString();
                endDayStr = LocalDate.now().toString();
            }
            return new TimeInterval(arrivalDayStr, endDayStr);
        }
    }

}
