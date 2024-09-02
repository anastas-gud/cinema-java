package ru.gudoshnikova.mainproject.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gudoshnikova.mainproject.model.FilmSessionsSeats;
import ru.gudoshnikova.mainproject.model.Order;

import java.util.Properties;

@Service
@Transactional
public class EmailService {
    public void sendEmail(Order order) {

        String to = order.getUser().getEmail();
        String from = "nastya.gudoshnikova@list.ru";
        String host = "smtp.mail.ru";
        String port = "587";

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.transport.protocol", "stmp");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

//        properties.put("mail.smtp.ssl.checkserveridentity", true);
//        properties.put("mail.smtp.ssl.trust", "*");
//        properties.put("mail.smtp.ssl.enable", "true");
//        properties.setProperty("mail.password", "rzQzqk3cCndpRnMkse1X");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "1WRzHkFYGzwws8rZ01uz");
            }
        };

        Session session = Session.getInstance(properties, auth);

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("Покупка на сервисе CINEMA centre");

            String text="Ваш заказ успешно оформлен.\n"+
                    "Вы преобрели билет на фильм: " + order.getFilmSession().getFilm().getTitle()+"\n"+
                    "Дата: " + order.getFilmSession().getStartDate() +
                    ", Время: " + order.getFilmSession().getStartTime() + "\n";

            for(FilmSessionsSeats seats: order.getSeats()){
                text+="Ряд: "+seats.getSeat().getRow() +", Место: " + seats.getSeat().getSeat()+"\n";
            }
            text+="Спасибо за покупку! ПРИЯТНОГО ПРОСМОТРА!";
            message.setText(text);

            Transport.send(message);
            System.out.println("Email Sent successfully....");
        } catch (MessagingException mex){ mex.printStackTrace(); }
    }
}
