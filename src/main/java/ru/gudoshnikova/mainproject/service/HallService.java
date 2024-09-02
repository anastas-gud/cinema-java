package ru.gudoshnikova.mainproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gudoshnikova.mainproject.model.Cinema;
import ru.gudoshnikova.mainproject.model.Hall;
import ru.gudoshnikova.mainproject.model.Seat;
import ru.gudoshnikova.mainproject.repository.HallRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class HallService {
    private final HallRepository hallRepository;
    private final SeatService seatService;
    @Autowired
    public HallService(HallRepository hallRepository, SeatService seatService) {
        this.hallRepository = hallRepository;
        this.seatService = seatService;
    }
    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }
    public List<Hall> getAllAvailableHalls(){
        return hallRepository.getAllAvailableHalls();
    }
    public List<Hall> getHallsByIsDeletedAndCinema(Cinema cinema){
        return hallRepository.getHallByIsDeletedAndCinema(false, cinema);
    }
    public Hall getHallById(int id) {
        return hallRepository.findById(id).orElse(null);
    }
    public Set<Seat> addSeatsInHall(Hall hall) {
        Set<Seat> seatSet=new HashSet<>();
        for(int i=1; i<=hall.getRowsCount(); i++){
            for(int j=1; j<=hall.getPlacesCount(); j++){
                Seat seat=new Seat();
                seat.setRow(i);
                seat.setSeat(j);
                seat.setHall(hall);
                seatSet.add(seat);
            }
        }
        return seatSet;
    }
    public void addHall(Hall hall, List<String> list) {
        hallRepository.save(hall);
        for(int i=1; i<=hall.getRowsCount(); i++){
            for(int j=1; j<=hall.getPlacesCount(); j++){
                Seat seat=new Seat();
                seat.setRow(i);
                seat.setSeat(j);
                seat.setHall(hall);
                for(String s:list){
                    String[] parts = s.split("-");
                    int row = Integer.parseInt(parts[0]);
                    int place = Integer.parseInt(parts[1]);
                    if(row==i && place==j){
                        seat.setExist(false);
                    }
                }
                seatService.createSeat(seat);
            }
        }
        System.out.println(hall);
    }
    public void updateHall(Hall hall) {
        hallRepository.save(hall);
    }
    public void deletedHall(int id) {
        getHallById(id).setDeleted(true);
        updateHall(getHallById(id));
    }
    public void deleteHall(int id) {
        hallRepository.deleteById(id);
    }
}
