package com.application.controller;

import com.application.model.requests.CreateUserRequest;
import com.application.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;

import static com.application.parser.ErrorsParser.getExceptionErrors;

@Controller
public class RegistrationController {
    final Logger logger = Logger.getLogger(RegistrationController.class);
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("errors") ArrayList<String> errors,
                                      CreateUserRequest createUserRequest) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@Valid CreateUserRequest createUserRequest,
                               BindingResult validResult,
                               @ModelAttribute("errors") ArrayList<String> errors,
                               RedirectAttributes redirectAttributes) {
        try {
            if (validResult.hasErrors()) {
                errors = new ArrayList<>() {{
                    add("Invalid username or password");
                }};
            } else {
                userService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword());
                return "redirect:/login";
            }
        } catch (Exception e) {
            errors = getExceptionErrors(e);
        } finally {
            redirectAttributes.addFlashAttribute("errors", errors);
            logger.info(errors.toString());
            return "redirect:registration";
        }
    }
}
