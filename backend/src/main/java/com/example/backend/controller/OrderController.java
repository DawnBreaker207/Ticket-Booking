package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.OrderDTO;
import com.example.backend.model.Order;
import com.example.backend.response.ResponseObject;
import com.example.backend.service.Impl.OrderServiceImpl;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
	this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity<List<Order>> getAll() {
	return ResponseEntity.status(HttpStatus.OK).body(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOne(@PathVariable String id) {
	return ResponseEntity.status(HttpStatus.OK).body(orderService.findOne(id));
    }

    @PostMapping("/init")
    public ResponseObject<String> initOrder(@RequestBody OrderDTO order) {
	return new ResponseObject<>(HttpStatus.OK, "Success", orderService.initOrder(order));
    }

    @PostMapping("/seatHold")
    public ResponseObject<Void> seatHold(@RequestBody OrderDTO o) {
	orderService.holdSeats(o.getOrderId(), o.getSeats(), o.getUserId());
	return new ResponseObject<>(HttpStatus.OK, "Success", null);
    }

    @PostMapping("/confirm")
    public ResponseObject<Order> confirm(@RequestBody OrderDTO o) {
	return new ResponseObject<>(HttpStatus.OK, "Success", orderService.confirm(o.getOrderId(), o.getUserId()));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable String id, @RequestBody Order o) {
	return ResponseEntity.status(HttpStatus.OK).body(orderService.update(id, o));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
	orderService.delete(id);
    }

}
