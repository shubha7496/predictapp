package com.hackethon.hackethon.dto;


public class PredictionResponseDTO {

    private String prediction;
    private String overspendDate;
    private Integer daysRemaining;
    private String recommendation;
	public String getPrediction() {
		return prediction;
	}
	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}
	public String getOverspendDate() {
		return overspendDate;
	}
	public void setOverspendDate(String overspendDate) {
		this.overspendDate = overspendDate;
	}
	public Integer getDaysRemaining() {
		return daysRemaining;
	}
	public void setDaysRemaining(Integer daysRemaining) {
		this.daysRemaining = daysRemaining;
	}
	public String getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

  
}

