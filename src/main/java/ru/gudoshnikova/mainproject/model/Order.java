package ru.gudoshnikova.mainproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Column(name = "time")
    @Temporal(TemporalType.TIME)
    private LocalTime time;

    @ManyToOne()
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "orders_user_id_fkey"), nullable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "film_session_id", foreignKey = @ForeignKey(name = "orders_film_session_id_fkey"), nullable = false)
    private FilmSession filmSession;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE
            })
    @JoinTable(name = "orders_seats",
            joinColumns = { @JoinColumn(name = "order_id") },
            inverseJoinColumns = { @JoinColumn(name = "film_session_seat_id") })
    private Set<FilmSessionsSeats> seats;

    public Order(){}

    public Order(int id, LocalDate date, LocalTime time, User user, FilmSession filmSession, Set<FilmSessionsSeats> seats) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.user = user;
        this.filmSession = filmSession;
        this.seats = seats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FilmSession getFilmSession() {
        return filmSession;
    }

    public void setFilmSession(FilmSession filmSession) {
        this.filmSession = filmSession;
    }

    public Set<FilmSessionsSeats> getSeats() {
        return seats;
    }

    public void setSeats(Set<FilmSessionsSeats> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

//    @Override
//    public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//        jsonGenerator.writeFieldName("id");
//        jsonGenerator.writeNumber(id);
//
//        jsonGenerator.writeFieldName("date");
//        if (date == null) {
//            jsonGenerator.writeString("");
//        } else {
//            Class<?> cls = date.getClass();
//            serializerProvider.findTypedValueSerializer(cls, true, null).serialize(date, jsonGenerator, serializerProvider);
//        }
//        jsonGenerator.writeFieldName("time");
//        if (time == null) {
//            jsonGenerator.writeString("");
//        } else {
//            Class<?> cls = time.getClass();
//            serializerProvider.findTypedValueSerializer(cls, true, null).serialize(time, jsonGenerator, serializerProvider);
//        }
//        jsonGenerator.writeFieldName("user");
//        if (user == null) {
//            jsonGenerator.writeString("");
//        } else {
//            Class<?> cls = user.getClass();
//            serializerProvider.findTypedValueSerializer(cls, true, null).serialize(user, jsonGenerator, serializerProvider);
//        }
//        jsonGenerator.writeFieldName("filmSession");
//        if (filmSession == null) {
//            jsonGenerator.writeString("");
//        } else {
//            Class<?> cls = filmSession.getClass();
//            serializerProvider.findTypedValueSerializer(cls, true, null).serialize(filmSession, jsonGenerator, serializerProvider);
//        }
//        jsonGenerator.writeFieldName("seats");
//        if (seats == null) {
//            jsonGenerator.writeString("");
//        } else {
//            Class<?> cls = seats.getClass();
//            serializerProvider.findTypedValueSerializer(cls, true, null).serialize(seats, jsonGenerator, serializerProvider);
//        }
//    }
//
//    @Override
//    public void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException {
//        serialize(jsonGenerator, serializerProvider);
//    }
}
