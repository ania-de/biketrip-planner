package com.biketrip.biketrip_planner.controller;

import com.biketrip.biketrip_planner.classes.Category;
import com.biketrip.biketrip_planner.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;
    public CategoryController(CategoryService service){ this.service = service; }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody @Valid Category c){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(c));
    }

    @GetMapping
    public List<Category> all(){ return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Category> get(@PathVariable Long id){
        return service.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/description")
    public Category updateDesc(@PathVariable Long id, @RequestBody Map<String,String> body){
        return service.updateDescription(id, body.getOrDefault("description",""));
    }
}
