package com.hackethon.hackethon.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hackethon.hackethon.dto.BudgetItemCreateDTO;
import com.hackethon.hackethon.dto.BudgetSummaryDTO;
import com.hackethon.hackethon.dto.PredictionResponseDTO;
import com.hackethon.hackethon.entity.BudgetItem;
import com.hackethon.hackethon.service.BudgetService;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
@CrossOrigin(origins = "*")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping
    public BudgetItem addBudgetItem(@RequestBody BudgetItemCreateDTO dto) {
        return budgetService.addBudgetItem(dto);
    }

    @GetMapping
    public List<BudgetItem> getAllItems() {
        return budgetService.getAllItems();
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable String id) {
        budgetService.deleteItem(id);
        return "Budget item deleted successfully";
    }

    @GetMapping("/summary")
    public BudgetSummaryDTO getSummary() {
        return budgetService.getBudgetSummary();
    }

    @GetMapping("/prediction")
    public PredictionResponseDTO getPrediction() {
        return budgetService.predictOverspend();
    }
}

