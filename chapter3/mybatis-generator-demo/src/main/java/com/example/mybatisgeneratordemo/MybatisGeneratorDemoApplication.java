package com.example.mybatisgeneratordemo;

import com.example.mybatisgeneratordemo.mapper.CoffeeMapper;
import com.example.mybatisgeneratordemo.model.Coffee;
import com.example.mybatisgeneratordemo.model.CoffeeExample;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@MapperScan("com.example.mybatisgeneratordemo.mapper")
@Slf4j
public class MybatisGeneratorDemoApplication implements ApplicationRunner {

    @Autowired
    private CoffeeMapper coffeeMapper;
    public static void main(String[] args) {
        SpringApplication.run(MybatisGeneratorDemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        generateArtifacts();
        playWithArtifacts();
    }

    private void playWithArtifacts() {
        Coffee espresso = new Coffee().withName("espresso")
                .withPrice(Money.of(CurrencyUnit.of("CNY"), 20.0))
                .withCreateTime(new Date())
                .withUpdateTime(new Date());
        coffeeMapper.insert(espresso);

        Coffee latte = new Coffee().withName("latte")
                .withPrice(Money.of(CurrencyUnit.of("CNY"), 30.0))
                .withCreateTime(new Date())
                .withUpdateTime(new Date());
        coffeeMapper.insert(latte);

        Coffee s = coffeeMapper.selectByPrimaryKey(1L);
        log.info("Coffee {}", s);

        CoffeeExample coffeeExample = new CoffeeExample();
        coffeeExample.createCriteria().andNameEqualTo("latte");
        List<Coffee> list = coffeeMapper.selectByExample(coffeeExample);
        list.forEach(e -> log.info("selectByExample: {}", e));
    }

    private void generateArtifacts() throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
        List<String> warnings = new ArrayList<>();
        ConfigurationParser configurationParser = new ConfigurationParser(warnings);
        Configuration conf = configurationParser.parseConfiguration(getClass().getResourceAsStream("/generatorConfig.xml"));
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator generator = new MyBatisGenerator(conf, callback, warnings);
        generator.generate(null);
    }
}
