package com.biketrip.biketrip_planner.exam;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @GetMapping("/upperletter")
    public String toUpperLetter(@RequestParam String name ) {
        return name.toUpperCase();
    }
}
