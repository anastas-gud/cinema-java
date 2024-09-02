package ru.gudoshnikova.mainproject.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gudoshnikova.mainproject.dto.DatePeriodDTO;
import ru.gudoshnikova.mainproject.dto.OrderDTO;
import ru.gudoshnikova.mainproject.model.FilmSessionsSeats;
import ru.gudoshnikova.mainproject.model.Order;
import ru.gudoshnikova.mainproject.repository.OrderRepository;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final CardService cardService;
    private final UserService userService;
    private final FilmSessionService filmSessionService;
    private final FilmSessionsSeatsService filmSessionsSeatsService;
    //private final KafkaTemplate<String, OrderDTO> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, OrderDTO, OrderDTO> replyingKafkaTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, CardService cardService,
                        UserService userService,
                        ReplyingKafkaTemplate replyingKafkaTemplate,
                        ModelMapper modelMapper, FilmSessionService filmSessionService,
                        FilmSessionsSeatsService filmSessionsSeatsService) {
        this.orderRepository = orderRepository;
        this.cardService = cardService;
        this.userService = userService;
        //this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
        this.filmSessionService = filmSessionService;
        this.filmSessionsSeatsService = filmSessionsSeatsService;
        this.replyingKafkaTemplate=replyingKafkaTemplate;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order createOrder(Order order){
        return orderRepository.save(order);
    }

    public Order saveOrder(Order order, Double points, Double pointForBalance) throws ExecutionException, InterruptedException, TimeoutException {
        OrderDTO orderDTO = convertOrderToDTO(order, points, pointForBalance);

        ProducerRecord<String, OrderDTO> record = new ProducerRecord<>("topic-1", orderDTO);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "topic-2".getBytes()));
        RequestReplyFuture<String, OrderDTO, OrderDTO> sendAndReceive = replyingKafkaTemplate.sendAndReceive(record);

        OrderDTO res=sendAndReceive.get(1000, TimeUnit.SECONDS).value();

        return (res != null)?convertDTOToOrder(res):null;
    }
    @KafkaListener(topics = "topic-1", groupId = "group1", containerFactory = "userKafkaListenerContainerFactory")
    @SendTo("topic-2")
    public Object saveOrderListener(ConsumerRecord<String, OrderDTO> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation){
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlation);
        OrderDTO orderdto=record.value();
        Order order = convertDTOToOrder(orderdto);
        for (FilmSessionsSeats filmSessionsSeats : order.getSeats()) {
            if (filmSessionsSeats.isBooked()) {
                return KafkaNull.INSTANCE;
            }
        }
        order.getUser().setBonuses(order.getUser().getBonuses() - orderdto.getPoints());
        order.getUser().setBonuses(order.getUser().getBonuses() + orderdto.getPointForBalance());
        order.getUser().setTotalSum(order.getUser().getTotalSum() + order.getFilmSession().getPrice() - orderdto.getPoints());
        if (order.getUser().getTotalSum() >= cardService.getCardById(3).getSumFrom()) {
            order.getUser().setCard(cardService.getCardById(3));
        } else if (order.getUser().getTotalSum() >= cardService.getCardById(2).getSumFrom()) {
            order.getUser().setCard(cardService.getCardById(2));
        }
        userService.save(order.getUser());
        for (FilmSessionsSeats seat : order.getSeats()) {
            seat.setBooked(true);
            filmSessionsSeatsService.updateFilmSessionsSeats(seat);
        }
        return convertOrderToDTO(createOrder(order), orderdto.getPoints(), orderdto.getPointForBalance());
    }

    public void deleteOrderById(int id) {
        orderRepository.deleteById(id);
    }
    public Double getTotalSum(DatePeriodDTO dto) {
        return orderRepository.getTotalCost(dto.getStartDate(), dto.getEndDate());
    }
    public Integer getTotalTickets(DatePeriodDTO dto) {
        return orderRepository.getTotalTickets(dto.getStartDate(), dto.getEndDate());
    }


    private OrderDTO convertOrderToDTO(Order order, Double points, Double pointForBalance) {
        return new OrderDTO(order.getId(),
                order.getDate(),
                order.getTime(),
                order.getUser().getId(),
                order.getFilmSession().getId(),
                order.getSeats().stream().map(FilmSessionsSeats::getId).collect(Collectors.toSet()),
                points, pointForBalance);
    }
    private Order convertDTOToOrder(OrderDTO dto) {
        return new Order(dto.getId(),
                dto.getDate(),
                dto.getTime(),
                userService.findById(dto.getUserId()),
                filmSessionService.getFilmSession(dto.getFilmSessionId()),
                dto.getFilmSessionsSeatsId().stream().map(filmSessionsSeatsService::getFilmSessionsSeatsById).collect(Collectors.toSet()));
    }
}
