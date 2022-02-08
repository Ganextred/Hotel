package com.example.luxuryhotel.model;


import com.example.luxuryhotel.entities.Apartment;
import com.example.luxuryhotel.entities.ApartmentStatus;
import com.example.luxuryhotel.entities.Status;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.repository.ApartmentRepository;
import com.example.luxuryhotel.repository.ApartmentStatusRepository;
import com.example.luxuryhotel.repository.UserRepository;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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
    Validator valid;

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

    @Transactional
    public List<String> book(String arrivalDay, String endDay,User user, Apartment apartment) {
        List<String> status = valid.bookApartment(arrivalDay,endDay,apartment);
        if (status.size()!=0)
            return status;
        else {
            ApartmentStatus apartmentStatus =
                    new ApartmentStatus(apartment,user,LocalDate.parse(arrivalDay),LocalDate.parse(endDay),
                           LocalDateTime.now().plusDays(2), Status.BOOKED);
            apartmentStatusRepo.save(apartmentStatus);
        }
        return status;
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
