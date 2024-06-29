package com.weshare.model;

import com.weshare.util.MoneyConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bills", schema = "weshare")
public class Bill implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @Convert(converter = MoneyConverter.class)
    private double ownAmount;

    @Convert(converter = MoneyConverter.class)
    private double amount;

    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private Category category;

    private LocalDate date;
    private boolean paid;
    private String description;
}