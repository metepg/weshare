package com.weshare.repository;

import com.weshare.model.Bill;
import com.weshare.model.Group;
import com.weshare.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findAllByDateBetween(LocalDate startDate, LocalDate endDate, Sort sort);

    List<Bill> findBillsByGroupAndPaidIsFalse(Group group);

    @Query("SELECT bill FROM Bill bill WHERE bill.owner = :owner AND bill.category != -1")
    List<Bill> findByOwnerAndCategory(@Param("owner") String owner);

    List<Bill> findBillsByOwner(User user);

    @Modifying
    @Transactional
    @Query("UPDATE Bill bill SET bill.paid = true WHERE bill.paid = false")
    void payDebt();

    @Query("SELECT b FROM Bill b WHERE "
            + "(:description IS NULL OR b.description ILIKE %:description%) AND "
            + "(COALESCE(:categories, NULL) IS NULL OR b.category IN :categories) AND "
            + "(COALESCE(:users, NULL) IS NULL OR b.owner IN :users)")
    List<Bill> findByFilter(
            @Param("description") String description,
            @Param("categories") List<Integer> categories,
            @Param("users") List<User> users);
}