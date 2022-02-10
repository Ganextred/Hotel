package com.example.luxuryhotel.model;

import com.example.luxuryhotel.entities.Apartment;
import com.example.luxuryhotel.entities.ApartmentStatus;
import com.example.luxuryhotel.entities.Clazz;
import com.example.luxuryhotel.entities.User;
import com.example.luxuryhotel.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class Validator {
    @Autowired
    private UserRepository userRepository;
    private final static Logger logger = Logger.getLogger(Validator.class);

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile("^[a-zA-Z0-9_]{4,30}$");
    private static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");

    public static boolean email(String email){return (email!=null && email.matches(VALID_EMAIL_ADDRESS_REGEX.pattern()));}
    public static boolean userName(String un){return (un!=null && un.matches(VALID_USERNAME_REGEX.pattern()));}
    public static boolean password(String pass){return (pass!=null && pass.matches(VALID_PASSWORD_REGEX.pattern()));}

    public List<String> regUser(User user){
        List<String> s=new ArrayList<>();
        if (!userName(user.getUsername())) {
            s.add("incorrectUsername");
        }
        if (!email(user.getEmail())) {
            s.add("Incorrect_email");
        }
        if (!password(user.getPassword())){
            s.add("Password_problem");
        }
        User userFromDb = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (userFromDb != null) {
            s.add("User_already_exist");
        }
        return s;
    }

    public List<String> bookApartment(String arrivalDayStr, String endDayStr, Apartment apartment){
        List<String> messages=new ArrayList<>();
        try{
            LocalDate arrivalDateT = LocalDate.parse(arrivalDayStr);
            LocalDate endDateT = LocalDate.parse(endDayStr);
            if (arrivalDateT.compareTo(endDateT)>0){
                messages.add("wrongDayOrder");
            }else {
                for (ApartmentStatus as: apartment.getApartmentStatuses()){
                    if ((as.getArrivalDay().compareTo(endDateT) <= 0 && as.getEndDay().compareTo(arrivalDateT)>=0 &&
                            (as.getPayTimeLimit() == null || as.getPayTimeLimit().compareTo(LocalDateTime.now()) >=0))
                            || arrivalDateT.compareTo(LocalDate.now()) <=0
                            || endDateT.compareTo(LocalDate.now()) <=0){
                        messages.add("apartmentNotAvailableOnTime");
                        break;
                    }
                };
            }
        }catch (DateTimeParseException e){
            messages.add("weCantRecognizeDay");
            logger.warn("Validator got unparsed arrival or end day");
        }catch (NullPointerException e){
            messages.add("chooseArrivalOrEndDay");
            logger.warn("Validator got null(or 0length) arrival or end day");
        }
        return messages;
    }
    public List<String> sendRequest(String arrivalDay, String endDay, Clazz clazz, Integer beds, String wishes){
        List<String> messages = new ArrayList<>();
        if(clazz == null)
            messages.add("choseClazz");
        if(beds == null || beds < 0)
            messages.add("incorrectBeds");
        if(wishes == null)
            messages.add("writeWishes");
        messages.addAll(timeInterval(arrivalDay,endDay));
        return messages;
    }

    public List<String> timeInterval(String arrivalDayStr, String endDayStr){
        List<String> messages = new ArrayList<>();
        try {
            LocalDate arrivalDateT = LocalDate.parse(arrivalDayStr);
            LocalDate endDateT = LocalDate.parse(endDayStr);
            if (arrivalDateT.compareTo(endDateT) > 0) {
                messages.add("wrongDayOrder");
            }
        }catch (DateTimeParseException e){
            messages.add("weCantRecognizeDay");
            logger.warn("Validator got unparsed arrival or end day");
        }catch (NullPointerException e){
            messages.add("chooseArrivalOrEndDay");
            logger.warn("Validator got null(or 0length) arrival or end day");
        }
        return messages;
    }
}
