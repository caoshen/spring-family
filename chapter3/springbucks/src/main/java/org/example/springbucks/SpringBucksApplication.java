package org.example.springbucks;

import lombok.extern.slf4j.Slf4j;
import org.example.springbucks.model.Coffee;
import org.example.springbucks.model.CoffeeOrder;
import org.example.springbucks.model.OrderState;
import org.example.springbucks.repository.CoffeeRepository;
import org.example.springbucks.service.CoffeeOrderService;
import org.example.springbucks.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories
public class SpringBucksApplication implements ApplicationRunner {

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private CoffeeService coffeeService;

    @Autowired
    private CoffeeOrderService coffeeOrderService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBucksApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("All coffee: {}", coffeeRepository.findAll());

        Optional<Coffee> latte = coffeeService.findOneCoffee("Latte");
        if (latte.isPresent()) {
            CoffeeOrder order = coffeeOrderService.createOrder("Li Lei", latte.get());
            log.info("Coffee order: {}", order);
            log.info("Update INIT to PAID: {}", coffeeOrderService.updateState(order, OrderState.PAID));
            log.info("Update PAID to INIT: {}", coffeeOrderService.updateState(order, OrderState.INIT));
        }
    }
}
