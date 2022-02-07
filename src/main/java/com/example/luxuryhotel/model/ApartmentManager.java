package com.example.luxuryhotel.model;


import com.example.luxuryhotel.contrloller.RegisterController;
import com.example.luxuryhotel.entities.Apartment;
import com.example.luxuryhotel.repository.ApartmentRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ApartmentManager {
    @Autowired
    ApartmentRepository apartmentRepo;

    private final static Logger logger = Logger.getLogger(ApartmentManager.class);

    public List<Apartment> getDefaultApartments(){
        Map<String,Boolean> m = new HashMap<>();
        m.put ("AVAILABLE", true);
        return getSortedApartments(LocalDate.now().toString(), LocalDate.now().toString(), new String[]{"price"}, new Boolean[]{true}, m);
    }

    public List<Apartment> getSortedApartments(String arrivalDay, String endDay, String[] sortParams, Boolean[] orderParams, Map<String, Boolean> status){
        Sort sortBySortParams = getSort(sortParams, orderParams);
        TimeInterval timeInterval = TimeInterval.getValidInterval(arrivalDay, endDay);
        return apartmentRepo.
                findWthStatus(timeInterval.arrivalDay, timeInterval.endDay,
                                status.containsKey("AVAILABLE"), status.containsKey("BOOKED"),
                                status.containsKey("BOUGHT"), status.containsKey("INACCESSIBLE"),
                                sortBySortParams);
    }

    private Sort getSort (String[] sortParams, Boolean[] orderParams){
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



    public static class TimeInterval{
        LocalDate arrivalDay;
        LocalDate endDay;

        private TimeInterval(String arrivalDayStr, String endDayStr) {
            this.arrivalDay = LocalDate.parse(arrivalDayStr);
            this.endDay = LocalDate.parse(endDayStr);
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
