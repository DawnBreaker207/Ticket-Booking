package com.example.backend.controller;

import com.example.backend.config.response.ResponseObject;
import com.example.backend.dto.request.OrderFilterDTO;
import com.example.backend.dto.response.OrderResponseDTO;
import com.example.backend.model.Order;
import com.example.backend.service.Impl.OrderServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Tag(name = "Order", description = "Operations related to order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @GetMapping("")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseObject<List<OrderResponseDTO>> getAll(@ModelAttribute OrderFilterDTO o) {
        return new ResponseObject<>(HttpStatus.OK, "Success", orderService.findAll(o));
    }

    @GetMapping("/{id}")
    public ResponseObject<Order> getOne(@PathVariable String id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", orderService.findOne(id));
    }

    @PostMapping("/init")
    public ResponseObject<String> initOrder(@RequestBody OrderResponseDTO order) {
        return new ResponseObject<>(HttpStatus.OK, "Success", orderService.initOrder(order));
    }

    @PostMapping("/seatHold")
    public ResponseObject<Void> seatHold(@RequestBody OrderResponseDTO o) {
        orderService.holdSeats(o.getOrderId(), o.getSeats(), o.getUserId());
        return new ResponseObject<>(HttpStatus.OK, "Success", null);
    }

    @PostMapping("/confirm")
    public ResponseObject<Order> confirm(@RequestBody OrderResponseDTO o) {
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
