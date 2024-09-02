package ru.gudoshnikova.mainproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gudoshnikova.mainproject.model.Card;
import ru.gudoshnikova.mainproject.repository.CardRepository;

import java.util.List;

@Service
@Transactional
public class CardService {
    private final CardRepository cardRepository;
    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    public Card getCardById(int id) {
        return cardRepository.findById(id).orElse(null);
    }
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }
    public void deleteCardById(int id) {
        cardRepository.deleteById(id);
    }
}
