package com.example.luxuryhotel.contrloller;


import com.example.luxuryhotel.entities.*;
import com.example.luxuryhotel.model.service.ApartmentManager;
import com.example.luxuryhotel.model.command.AnswerRequestCommand;
import com.example.luxuryhotel.model.command.CommandFactory;
import com.example.luxuryhotel.model.command.ConfirmBookCommand;
import com.example.luxuryhotel.model.repository.ApartmentRepository;
import com.example.luxuryhotel.model.repository.ApartmentStatusRepository;
import com.example.luxuryhotel.model.repository.RequestRepository;
import com.example.luxuryhotel.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    ApartmentRepository apartmentRepo;
    @Autowired
    ApartmentStatusRepository apartmentStatusRepo;
    @Autowired
    CommandFactory commandFactory;
    @Autowired
    RequestRepository requestRepo;
    @Autowired
    ApartmentManager apartmentManager;

    @GetMapping("/adminPanel")
    public String adminPanel(Model model){
        model.addAttribute("users", userRepo.findAll());
        List<ApartmentStatus> bStatuses = apartmentStatusRepo.findApartmentStatusByStatusAndPayTimeLimitAfter(Status.BOOKED, LocalDateTime.now());
        bStatuses.addAll(apartmentStatusRepo.findApartmentStatusByStatusAndPayTimeLimitAfter(Status.BOOKEDREQUEST, LocalDateTime.now()));
        List<Request> requests = requestRepo.findByAndAnswerStatusIsNull();
        model.addAttribute("bStatuses", bStatuses);
        model.addAttribute("requests", requests);
        model.addAttribute("apartments", apartmentRepo.findAll());
        model.addAttribute("existingRoles",Role.values());
        return "adminPanel";
    }
    @PostMapping("/editUser")
    public String editUser(Model model, @RequestParam(name = "userId", required = false) User user,@RequestParam Map<String, Boolean> roles ){
        user = userRepo.findByUsername("user");
        user.getRoles().add(Role.ADMIN);
        userRepo.save(user);
        return "redirect:/admin/adminPanel";
    }

    @PostMapping("confirmStatus")
    public String confirmBook(@RequestParam(name = "status") ApartmentStatus apartmentStatus){
        ConfirmBookCommand command= commandFactory.getConfirmBookCommand(apartmentStatus);
        command.execute();
        return"redirect:/admin/adminPanel";
    }

    @GetMapping("/seeRequest/")
    public String editUser(Model model,@RequestParam(name = "page", required = false, defaultValue = "1" ) Integer page, @RequestParam Request request){
        if (page <1)
            page =1;
        if (!model.containsAttribute("apartments")) {
            apartmentManager.addDefaultModelSortParams(model, request.getArrivalDay().toString(), request.getEndDay().toString(), page);
        }
        model.addAttribute("request",request);
        return "seeRequest";
    }
    @PostMapping("/seeRequest/applySort")
    public String applySort (@RequestParam (name = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(name="request") Request request,
                             @RequestParam(name = "sortParam[]", required=false) String[] sortParams,
                             @RequestParam(name = "orderParam[]") Boolean[] orderParams,
                             RedirectAttributes rA){
        if (page <1)
            page =1;
        Map<String, Boolean> status = new HashMap<>();
        status.put("AVAILABLE", true);
        apartmentManager.addModelFlashSortParams(rA,request.getArrivalDay().toString(), request.getEndDay().toString(), sortParams, orderParams, status, page);
        return "redirect:/admin/seeRequest/?request="+request.getId().toString()+"&page="+page.toString();
    }
    @PostMapping("/answerRequest")
    public String answerRequest (@RequestParam(name="request") Request request,
                                 @RequestParam(name="apartment") Apartment apartment){
        AnswerRequestCommand command = commandFactory.getAnswerRequestCommand(request, apartment);
        List<String> messages = command.execute();
        if (messages.size() != 0) {
            System.out.println(messages);
            return "redirect:/admin/seeRequest/?request=" + request.getId().toString();
        }
        return "redirect:/admin/adminPanel";
    }

    @GetMapping("/editApartment/")
    public String apartment(Model model, @RequestParam Apartment apartment){
        model.addAttribute("apartment", apartment);
        return "editApartment";
    }

    @PostMapping("/editApartment/save/")
    public String applySort ( @RequestParam Apartment apartment,
                              @RequestParam (name = "price", required=true) Integer price,
                              @RequestParam (name = "beds", required=true) Integer beds,
                              @RequestParam (name = "clazz", required=true) Clazz clazz,
                              @RequestParam("image") MultipartFile file,
                              RedirectAttributes rA){
        rA.addFlashAttribute("messages", new ArrayList<String>());
        List<String> messages = apartmentManager.updateApartment(apartment, price,clazz,beds, file);
        if (messages.size() != 0) {
            rA.addFlashAttribute("messages", messages);
            return ("redirect:/admin/editApartment/?apartment="+apartment.getId());
        } else{
            return ("redirect:/admin/editApartment/?apartment="+apartment.getId());
        }
    }

    @PostMapping("/newApartment")
    public String applySort (RedirectAttributes rA){
        rA.addFlashAttribute("messages", new ArrayList<String>());
        Apartment apartment = apartmentManager.newApartment();
        return ("redirect:/admin/editApartment/?apartment="+apartment.getId());
    }

    @PostMapping("/editApartment/delete/")
    public String applySort (@RequestParam Apartment apartment,RedirectAttributes rA){
        rA.addFlashAttribute("messages", new ArrayList<String>());
        apartmentManager.deleteApartment(apartment);
        return ("redirect:/admin/adminPanel");
    }
}