package com.weshare.service;

import com.weshare.model.Bill;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.Date;
import java.util.List;

@Service
public class BillService {

    public Bill create(Bill bill) {
//        return db.save(bill);
        return null;
    }

    public List<Bill> findAll() {
//        return db.findAll(Bill.class);
        return null;
    }

    public List<Bill> findAllFromLastSixMonths() {
//        Calendar calendar = Calendar.getInstance();
//        Date to = calendar.getTime();
//
//        calendar.add(Calendar.MONTH, -6);
//        Date from = calendar.getTime();
//
//        Query query = queryBetweenDates(from, to);
//        return db.find(query, Bill.class);
        return null;
    }

    public List<Bill> payDebt() {
//        Query query = queryAllUnpaid();
//
//        Update update = new Update();
//        update.set("isPaid", true);
//        db.updateMulti(query, update, Bill.class);
//
//        return findAllFromLastSixMonths();
        return null;
    }

    public double getTotalDebtByName(String name) {
//        Query query = queryAllUnpaid();
//        List<Bill> bills = db.find(query, Bill.class);
//
//        return bills
//                .stream()
//                .mapToDouble(bill -> getOwnAmount(bill, name))
//                .sum();
        return 0;
    }

    public List<Bill> findAllByYear(Integer year) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, year);
//        calendar.set(Calendar.DAY_OF_YEAR, 1);
//        Date from = calendar.getTime();
//
//        calendar.set(Calendar.MONTH, 11);
//        calendar.set(Calendar.DAY_OF_MONTH, 31);
//        Date to = calendar.getTime();
//
//        Query query = queryBetweenDates(from, to);
//
//        return db.find(query, Bill.class);
        return null;
    }

    private Query queryAllUnpaid() {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("isPaid").is(false));
//        return query;
        return null;
    }

    private Query queryBetweenDates(Date from, Date to) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("date").gte(from).lte(to));
//        return query;
        return null;
    }

    private double getOwnAmount(Bill bill, String name) {
//        double total = bill.getAmount() - bill.getOwnAmount();
//        return bill.getOwner().equals(name)
//                ? total
//                : -total;
        return 0;
    }

}
