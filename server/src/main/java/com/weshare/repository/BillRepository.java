package com.weshare.repository;

import com.weshare.model.Bill;
import com.weshare.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    List<Bill> findAllByDateBetween(LocalDate startDate, LocalDate endDate, Sort sort);

    @Query("SELECT b FROM Bill b " +
            "JOIN b.owner u " +
            "JOIN u.group g " +
            "WHERE g.id = :groupId " +
            "ORDER BY b.date DESC")
    List<Bill> findRecentBills(@Param("groupId") UUID groupId, Pageable pageable);

    List<Bill> findBillsByOwner(User user);

    @Query("SELECT bill FROM Bill bill WHERE YEAR(bill.date) = :year")
    List<Bill> findAllByYear(@Param("year") Integer year);

    @Modifying
    @Transactional
    @Query("UPDATE Bill bill SET bill.paid = true WHERE bill.paid = false")
    void payDebt();

    /**
     * Finds bills by description, categories, and users, with sorting.
     *
     * @param description The bill description to filter (optional).
     * @param categories  List of category IDs to filter (optional).
     * @param users       List of users to filter (optional).
     * @param sort        Sorting criteria.
     * @return List of bills matching the filters.
     */
    @Query("SELECT b FROM Bill b WHERE "
            + "(:description IS NULL OR b.description ILIKE %:description%) AND "
            + "(:#{#categories == null || #categories.isEmpty()} = true OR b.category.id IN :categories) AND "
            + "(:#{#users == null || #users.isEmpty()} = true OR b.owner IN :users)")
    List<Bill> findByFilter(
            @Param("description") String description,
            @Param("categories") List<Integer> categories,
            @Param("users") List<User> users,
            Sort sort);

    /**
     * Calculates the total debt of a user within their group.
     * The calculation involves summing up the differences between the amount and ownAmount
     * for bills owned by the user and subtracting the same for bills owned by other users in the group.
     * The amounts are divided by 100.0 to convert from int (cent) to double (euro).
     *
     * @param userId the ID of the user whose total debt is to be calculated
     * @return the total debt of the user within their group, in euros
     */
    @Query("SELECT " +
            "COALESCE((SUM(CASE WHEN b.owner.id = :userId THEN (b.amount - b.ownAmount) ELSE 0 END) - " +
            "SUM(CASE WHEN b.owner.id != :userId THEN (b.amount - b.ownAmount) ELSE 0 END)) / 100.0, 0) " +
            "FROM Bill b " +
            "WHERE b.owner.id IN (" +
            "SELECT u.id FROM User u WHERE u.group.id = (" +
            "SELECT u2.group.id FROM User u2 WHERE u2.id = :userId)) " +
            "AND b.paid = false")
    Double findUserDebtByUserId(@Param("userId") Integer userId);

}