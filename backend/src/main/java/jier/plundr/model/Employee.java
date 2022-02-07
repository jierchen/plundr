package jier.plundr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Employee extends User {

    @Id
    @GeneratedValue
    @Column(name = "employee_id")
    private Long id;

    // Employee information
    @Column(name = "salary")
    private BigDecimal salary;
}

