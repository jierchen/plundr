package jier.plundr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Employee extends User {

    // Employee information
    @Column(name = "salary")
    @Digits(integer = 7, fraction = 2)
    @PositiveOrZero
    private BigDecimal salary;
}

