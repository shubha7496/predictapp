package com.hackethon.hackethon.dto;


import java.util.Map;

public class BudgetSummaryDTO {

    private double totalIncome;
    private double totalExpenses;
    private double remainingBudget;
    private Map<String, Map<String, Double>> categories; // category -> {"income": x, "expenses": y}
	public double getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}
	public double getTotalExpenses() {
		return totalExpenses;
	}
	public void setTotalExpenses(double totalExpenses) {
		this.totalExpenses = totalExpenses;
	}
	public double getRemainingBudget() {
		return remainingBudget;
	}
	public void setRemainingBudget(double remainingBudget) {
		this.remainingBudget = remainingBudget;
	}
	public Map<String, Map<String, Double>> getCategories() {
		return categories;
	}
	public void setCategories(Map<String, Map<String, Double>> categories) {
		this.categories = categories;
	}
}

