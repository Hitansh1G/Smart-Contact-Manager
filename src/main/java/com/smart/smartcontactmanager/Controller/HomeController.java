package com.smart.smartcontactmanager.Controller;

import com.smart.helper.Message;
import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("title", "Home - Smart Contact");
        return "home";
    }
    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title", "About - Smart Contact");
        return "about";
    }
    @RequestMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title", "Register - Smart Contact");
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value="/do_register",method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               @RequestParam(value = "agreement",defaultValue = "false")
                               boolean agreement,
                               Model model,
                               HttpSession httpSession){
        try {
            if(!agreement){
                System.out.println("you have not agreed the terms and conditions ");
                throw new Exception("you have not agreed the terms and conditions ");
            }

            if(bindingResult.hasErrors()){
                System.out.println("error "+bindingResult.toString());
                model.addAttribute("user",user);
                return "signup";
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("default.png");

            System.out.println("Agreement"+ true);
            System.out.println("User"+user);

            User result = this.userRepository.save(user);

            model.addAttribute("user",new User());
            httpSession.setAttribute("message",new Message("Successfully Registered","alert-success"));
            return "signup";
        }
        catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user",user);
            httpSession.setAttribute("message",new Message("Something went wrong" + e.getMessage(),"alert-danger"));
            return "signup";
        }
    }

//    @Autowired
//    private UserRepository userRepository;
//
//    @GetMapping("/test")
//    @ResponseBody
//    public String test(){
//        User user = new User();
//        user.setName("Hitansh");
//        user.setEmail("hitansh@gmail.com");
//
//        userRepository.save(user);
//        return "working";
//    }
}
