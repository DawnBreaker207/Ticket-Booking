package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.request.OrderFilterDTO;
import com.example.backend.dto.shared.OrderDTO;
import com.example.backend.model.Order;
import com.example.backend.service.Impl.OrderServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Tag(name = "Order", description = "Operations related to order")
public class OrderController {
    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseObject<List<OrderDTO>> getAll(@ModelAttribute OrderFilterDTO o) {
        return new ResponseObject<>(HttpStatus.OK, "Success", orderService.findAll(o));
    }

    @GetMapping("/{id}")
    public ResponseObject<Order> getOne(@PathVariable String id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", orderService.findOne(id));
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
        return new ResponseObject<>(HttpStatus.OK, "Success", orderService.confirm(o));

    }

    @PutMapping("/{id}")
    public ResponseObject<Order> update(@PathVariable String id, @RequestBody Order o) {
        return new ResponseObject<>(HttpStatus.OK, "Success", orderService.update(id, o));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        orderService.delete(id);
    }

}
