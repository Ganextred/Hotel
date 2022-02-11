package com.example.luxuryhotel.contrloller;


import com.example.luxuryhotel.entities.*;
import com.example.luxuryhotel.model.ApartmentManager;
import com.example.luxuryhotel.model.command.AnswerRequestCommand;
import com.example.luxuryhotel.model.command.CommandFactory;
import com.example.luxuryhotel.model.command.ConfirmBookCommand;
import com.example.luxuryhotel.repository.ApartmentStatusRepository;
import com.example.luxuryhotel.repository.RequestRepository;
import com.example.luxuryhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserRepository userRepo;
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
        List<Request> requests = requestRepo.findByAndAnswerStatusIsNull();
        model.addAttribute("bStatuses", bStatuses);
        model.addAttribute("requests", requests);
        return "adminPanel";
    }
    @GetMapping("editUser/{user}")
    public String editUser(Model model, @PathVariable User user){
        model.addAttribute("user", user);
        model.addAttribute("existingRoles",Role.values());
        return "editUser";
    }
    @PostMapping("confirmStatus")
    public String confirmBook(@RequestParam(name = "status") ApartmentStatus apartmentStatus){
        ConfirmBookCommand command= commandFactory.getConfirmBookCommand(apartmentStatus);
        command.execute();
        return"redirect:/admin/adminPanel";
    }

    @GetMapping("/seeRequest/")
    public String editUser(Model model, @RequestParam Request request){
        if (!model.containsAttribute("apartments")) {
            Iterable<Apartment> apartments = apartmentManager.getDefaultApartments(request.getArrivalDay().toString(),request.getEndDay().toString());
            model.addAttribute("apartments", apartments);
        }
        model.addAttribute("request",request);
        return "seeRequest";
    }
    @PostMapping("/seeRequest/applySort")
    public String applySort (@RequestParam(name="request") Request request,
                             @RequestParam(name = "sortParam[]", required=false) String[] sortParams,
                             @RequestParam(name = "orderParam[]") Boolean[] orderParams,
                             RedirectAttributes rA){
        Map<String, Boolean> status = new HashMap<>();
        status.put("AVAILABLE", true);
        Iterable <Apartment> apartments = apartmentManager.getSortedApartments(request.getArrivalDay().toString(),request.getEndDay().toString(), sortParams, orderParams, status);
        rA.addFlashAttribute("apartments", apartments);
        return "redirect:/admin/seeRequest/?request="+request.getId().toString();
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

}