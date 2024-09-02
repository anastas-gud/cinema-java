package ru.gudoshnikova.mainproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gudoshnikova.mainproject.model.Hall;
import ru.gudoshnikova.mainproject.model.Seat;
import ru.gudoshnikova.mainproject.repository.SeatRepository;

import java.util.List;

@Service
@Transactional
public class SeatService {
    private final SeatRepository seatRepository;
    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }
    public List<Seat> getSeats() {
        return seatRepository.findAll();
    }
    public List<Seat> getSeatsByHall(Hall hall){
        return seatRepository.findByHall(hall);
    }
    public Seat getSeatById(int id) {
        return seatRepository.findById(id).orElse(null);
    }
    public Seat createSeat(Seat seat) {
        return seatRepository.save(seat);
    }
    public Seat updateSeat(Seat seat) {
        return seatRepository.save(seat);
    }
    public void deleteSeat(int id) {
        seatRepository.deleteById(id);
    }
}
