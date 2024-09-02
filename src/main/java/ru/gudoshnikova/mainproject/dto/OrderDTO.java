package ru.gudoshnikova.mainproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int id;

    private LocalDate date;

    private LocalTime time;

    private Integer userId;

    private Integer filmSessionId;

    private Set<Integer> filmSessionsSeatsId;

    private Double points;

    private Double pointForBalance;
}
