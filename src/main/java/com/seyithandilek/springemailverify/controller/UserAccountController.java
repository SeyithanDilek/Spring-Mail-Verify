package com.seyithandilek.springemailverify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.seyithandilek.springemailverify.Service.EmailSenderService;
import com.seyithandilek.springemailverify.model.ConfirmationToken;
import com.seyithandilek.springemailverify.model.User;
import com.seyithandilek.springemailverify.repository.ConfirmationTokenRepository;
import com.seyithandilek.springemailverify.repository.UserRepository;

@Controller
public class UserAccountController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	EmailSenderService emailSenderService;
	
	
	@GetMapping(value="/register")
	public ModelAndView displayRegistration(ModelAndView modelAndView, User user){
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }
	
	@PostMapping(value="/register")
	public ModelAndView registerUser(ModelAndView modelAndView, User user) {

        User existingUser = userRepository.findByEmailIdIgnoreCase(user.getEmailId());
        if(existingUser != null)
        {
            modelAndView.addObject("message","This email already exists!");
            modelAndView.setViewName("error");
        }
        else
        {
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(confirmationToken);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            
            mailMessage.setTo(user.getEmailId());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("info@example.com");
            mailMessage.setText("To confirm your account, please click here : "
            +"http://localhost:8088/confirm-account?token="+confirmationToken.getConfirmationToken());
            emailSenderService.sendEmail(mailMessage);
            modelAndView.addObject("emailId", user.getEmailId());
            modelAndView.setViewName("successfulRegisteration");
        }
        return modelAndView;
    }
	
     @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
	 public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
	    {
	        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

	        if(token != null)
	        {
	            User user = userRepository.findByEmailIdIgnoreCase(token.getUser().getEmailId());
	            user.setEnabled(true);
	            userRepository.save(user);
	            modelAndView.setViewName("accountVerified");
	        }
	        else
	        {
	            modelAndView.addObject("message","The link is invalid or broken!");
	            modelAndView.setViewName("error");
	        }

	        return modelAndView;
	    }
}
