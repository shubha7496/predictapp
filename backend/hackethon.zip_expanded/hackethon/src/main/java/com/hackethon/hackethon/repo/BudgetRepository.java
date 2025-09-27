package com.hackethon.hackethon.repo;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hackethon.hackethon.entity.BudgetItem;

import java.util.List;

@Repository
public interface BudgetRepository extends MongoRepository<BudgetItem, String> {

    List<BudgetItem> findAllByDateMonthAndDateYear(int month, int year);
}
