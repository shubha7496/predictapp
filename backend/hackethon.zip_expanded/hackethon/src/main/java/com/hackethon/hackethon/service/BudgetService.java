package com.hackethon.hackethon.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackethon.hackethon.dto.BudgetItemCreateDTO;
import com.hackethon.hackethon.dto.BudgetSummaryDTO;
import com.hackethon.hackethon.dto.PredictionResponseDTO;
import com.hackethon.hackethon.entity.BudgetItem;
import com.hackethon.hackethon.repo.BudgetRepository;

import com.hackethon.hackethon.repo.BudgetRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public BudgetItem addBudgetItem(BudgetItemCreateDTO dto) {
        BudgetItem item = new BudgetItem();
        item.setType(dto.getType());
        item.setCategory(dto.getCategory());
        item.setAmount(dto.getAmount());
        item.setDescription(dto.getDescription());
        item.setDate(dto.getDate());
        return budgetRepository.save(item);
    }

    public List<BudgetItem> getAllItems() {
        return budgetRepository.findAll();
    }

    public void deleteItem(String id) {
        budgetRepository.deleteById(id);
    }

    public BudgetSummaryDTO getBudgetSummary() {
        List<BudgetItem> items = budgetRepository.findAll();
        double totalIncome = 0;
        double totalExpenses = 0;
        Map<String, Map<String, Double>> categories = new HashMap<>();

        for (BudgetItem item : items) {
            if (item.getType().equalsIgnoreCase("income")) totalIncome += item.getAmount();
            else totalExpenses += item.getAmount();

            categories.putIfAbsent(item.getCategory(), new HashMap<>());
            Map<String, Double> cat = categories.get(item.getCategory());
            cat.put("income", cat.getOrDefault("income", 0.0));
            cat.put("expenses", cat.getOrDefault("expenses", 0.0));

            if (item.getType().equalsIgnoreCase("income")) cat.put("income", cat.get("income") + item.getAmount());
            else cat.put("expenses", cat.get("expenses") + item.getAmount());
        }

        BudgetSummaryDTO summary = new BudgetSummaryDTO();
        summary.setTotalIncome(totalIncome);
        summary.setTotalExpenses(totalExpenses);
        summary.setRemainingBudget(totalIncome - totalExpenses);
        summary.setCategories(categories);

        return summary;
    }

    public PredictionResponseDTO predictOverspend() {
        List<BudgetItem> items = budgetRepository.findAll();

        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        List<BudgetItem> currentMonthItems = new ArrayList<>();
        for (BudgetItem item : items) {
            if (item.getDate().getMonthValue() == month && item.getDate().getYear() == year) {
                currentMonthItems.add(item);
            }
        }

        double totalIncome = items.stream().filter(i -> i.getType().equalsIgnoreCase("income"))
                .mapToDouble(BudgetItem::getAmount).sum();

        double currentExpenses = currentMonthItems.stream().filter(i -> i.getType().equalsIgnoreCase("expense"))
                .mapToDouble(BudgetItem::getAmount).sum();

        double dailyAvg = today.getDayOfMonth() > 0 ? currentExpenses / today.getDayOfMonth() : 0;
        double remainingBudget = totalIncome - currentExpenses;

        PredictionResponseDTO response = new PredictionResponseDTO();
        response.setPrediction("AI prediction placeholder"); // You can call your AI API here
        if (dailyAvg > 0 && remainingBudget / dailyAvg < YearMonth.now().lengthOfMonth()) {
            int daysUntilOverspend = (int) (remainingBudget / dailyAvg);
            response.setOverspendDate("Day " + (today.getDayOfMonth() + daysUntilOverspend) + " of this month");
            response.setDaysRemaining(daysUntilOverspend);
            response.setRecommendation("Reduce spending in highest expense categories.");
        } else {
            response.setRecommendation("You are within budget this month.");
        }
        return response;
    }
}

