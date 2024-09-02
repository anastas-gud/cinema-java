package ru.gudoshnikova.mainproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gudoshnikova.mainproject.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
}
