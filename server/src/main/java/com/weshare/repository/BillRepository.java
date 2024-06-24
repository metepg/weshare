package com.weshare.repository;

import com.weshare.model.Bill;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findAllByDateBetween(LocalDate startDate, LocalDate endDate, Sort sort);

    @Query("SELECT bill FROM Bill bill WHERE bill.isPaid = false")
    List<Bill> findAllUnpaidBills();

    @Query("SELECT bill FROM Bill bill WHERE bill.owner = :owner AND bill.category != -1")
    List<Bill> findByOwnerAndCategory(@Param("owner") String owner);

    @Modifying
    @Query("UPDATE Bill bill SET bill.isPaid = true WHERE bill.isPaid = false")
    void payDebt();

    @Query("SELECT b FROM Bill b WHERE "
            + "(:description IS NULL OR b.description ILIKE %:description%) AND "
            + "(COALESCE(:categories, NULL) IS NULL OR b.category IN :categories) AND "
            + "(COALESCE(:users, NULL) IS NULL OR b.owner IN :users)")
    List<Bill> findByFilter(
            @Param("description") String description,
            @Param("categories") List<Integer> categories,
            @Param("users") List<String> users);
}