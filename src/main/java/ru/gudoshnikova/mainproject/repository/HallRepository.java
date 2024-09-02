package ru.gudoshnikova.mainproject.repository;

import org.graalvm.nativeimage.c.struct.RawPointerTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gudoshnikova.mainproject.model.Cinema;
import ru.gudoshnikova.mainproject.model.Film;
import ru.gudoshnikova.mainproject.model.Hall;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Integer> {
    @Query(nativeQuery = true,
            value = """
                    select distinct h.*
                    from halls h
                    join cinema c on c.id = h.cinema_id
                                        and c.is_deleted = false
                                        and h.is_deleted = false
            """)
    List<Hall> getAllAvailableHalls();
    List<Hall> getHallByIsDeletedAndCinema(boolean isDeleted, Cinema cinema);
}
