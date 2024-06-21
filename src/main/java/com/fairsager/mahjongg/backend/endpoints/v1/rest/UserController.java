package com.fairsager.mahjongg.backend.endpoints.v1.rest;

import com.fairsager.mahjongg.backend.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
}
