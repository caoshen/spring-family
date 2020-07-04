package com.example.pagehelper;

import com.example.pagehelper.mapper.CoffeeMapper;
import com.example.pagehelper.model.Coffee;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
@MapperScan("com.example.pagehelper.mapper")
public class PagehelperApplication implements ApplicationRunner {
    @Autowired
    private CoffeeMapper mapper;

    public static void main(String[] args) {
        SpringApplication.run(PagehelperApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mapper.findAllWithRowBounds(new RowBounds(1, 3))
                .forEach(coffee -> log.info("Page(1) Coffee {}", coffee));
        mapper.findAllWithRowBounds(new RowBounds(2, 3))
                .forEach(coffee -> log.info("Page(2) Coffee {}", coffee));
        log.info("========================");
        mapper.findAllWithRowBounds(new RowBounds(1, 0))
                .forEach(coffee -> log.info("Page(1) Coffee {}", coffee));
        log.info("========================");
        mapper.findAllWithParam(1, 3)
                .forEach(coffee -> log.info("Page(1) Coffee {}", coffee));
        List<Coffee> list = mapper.findAllWithParam(2, 3);
        PageInfo<Coffee> pageInfo = new PageInfo<>(list);
        log.info("PageInfo: {}", pageInfo);
    }
}
