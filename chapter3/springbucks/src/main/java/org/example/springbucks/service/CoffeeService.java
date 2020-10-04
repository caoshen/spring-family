package org.example.springbucks.service;

import lombok.extern.slf4j.Slf4j;
import org.example.springbucks.model.Coffee;
import org.example.springbucks.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("name",
                ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase());

        Coffee coffee = Coffee.builder().name(name).build();
        Optional<Coffee> optional = coffeeRepository.findOne(Example.of(coffee, exampleMatcher));
        log.info("Coffee found: {}", optional);
        return optional;
    }

}
