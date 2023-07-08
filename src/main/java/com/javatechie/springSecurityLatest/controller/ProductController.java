package com.javatechie.springSecurityLatest.controller;

import com.javatechie.springSecurityLatest.dto.AuthRequest;
import com.javatechie.springSecurityLatest.dto.Product;
import com.javatechie.springSecurityLatest.entity.UserInfo;
import com.javatechie.springSecurityLatest.service.JwtService;
import com.javatechie.springSecurityLatest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ProductService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome()
    {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/new")
    public String addNewUser(@RequestBody UserInfo userInfo){
        return service.addUser(userInfo);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getAllTheProducts()
    {
        return "Secured using spring security, cannot be accessed by user only admin can access it through jaga as user";
    }



    @GetMapping("/id")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String getProductsById()
    {
        return "User can access through his id";

    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest){
       Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
       if(authentication.isAuthenticated())
       {
           return jwtService.generateToken(authRequest.getUsername());
       }
       else {
           throw new UsernameNotFoundException("invalid user request!");
       }


    }

}
