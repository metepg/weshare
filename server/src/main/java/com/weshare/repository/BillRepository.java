package com.weshare.repository;

import com.weshare.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT b FROM Bill b WHERE b.date >= :startDate AND b.date <= :today")
    List<Bill> findAllFromLastSixMonths(@Param("startDate") LocalDate startDate, @Param("today") LocalDate today);

    List<Bill> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT b FROM Bill b WHERE b.isPaid = false")
    List<Bill> findAllUnpaidBills();

    @Modifying
    @Query("UPDATE Bill b SET b.isPaid = true WHERE b.isPaid = false")
    void payDebt();
}
