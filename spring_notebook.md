# 极客时间 spring 笔记

## 第一章 初识 Spring

### 04 | 编写你的第一个Spring程序

maven 打包

mvn clean package -Dmaven.test.skip

## 第二章：JDBC必知必会

### 05 | 如何配置单数据源

#### Lombok

如果 Slf4j log 找不到，需要在 Intellij Idea 添加 Lombok 插件。

Lombok 插件有可能和最新的 Intellij Idea 版本不兼容。比如 2019.3.4 社区版。

#### Endpoints

management.endpoints.web.exposure.include=* 可以暴露一些默认不在 Web 暴露的 Endpoints，这些默认只以 JMX 方式暴露。

#### spring.output.ansi

spring.output.ansi.enabled=ALWAYS 可以使 spring 的命令行输出彩色显示

#### h2

```
#spring datasource config. h2 memory db
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
```

#### hikari

```
# hikari connection
spring.datasource.hikari.maximumPoolSize=5
spring.datasource.hikari.minimumIdle=5
spring.datasource.hikari.idleTimeout=600000
spring.datasource.hikari.connectionTimeout=30000
spring.datasource.hikari.maxLifetime=1800000
```

### 06 | 如何配置多数据源

@Bean 指返回值当做一个 Bean。

@Resource 指方法的参数按照名字来注入其他的 Bean。

### 08 | 那些好用的连接池们：Alibaba Druid

druid 配置 filter

resources/META-INF/druid-filter.properties 配置 filter

```
druid.filters.conn=com.example.druiddemo.ConnectionLogFilter
```

在连接前和连接后打印日志

```java
public class ConnectionLogFilter extends FilterEventAdapter {

    @Override
    public void connection_connectBefore(FilterChain chain, Properties info) {
        System.out.println("BEFORE CONNECTION!");
    }

    @Override
    public void connection_connectAfter(ConnectionProxy connection) {
        System.out.println("AFTER CONNECTION!");
    }
}
```

[使用ConfigFilter](https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8ConfigFilter)

### 09 | 如何通过Spring JDBC访问数据库

SimpleJdbcInsert 简单 insert

NamedParameterJdbcTemplate 根据 bean 插入修改删除

网页查看 h2 数据库

```java
@Configuration
public class DataConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2WebServer() throws SQLException {
        return Server.createWebServer("-web", "-webAllowOthers", "-webDaemon", "-webPort", "8082");
    }
}
```

配置

```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
```

可以在 http://localhost:8082/ 查看数据库

### 11 | 什么是Spring的事务抽象（下）

编程式事务

```java
    @Override
    public void run(String... args) throws Exception {
        log.info("COUNT BEFORE TRANSACTION: {}", getCount());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'aaa')");
                log.info("COUNT IN TRANSACTION: {}", getCount());
                transactionStatus.setRollbackOnly();
            }
        });
        log.info("COUNT AFTER TRANSACTION: {}", getCount());
    }
```

声明式事务

```java
    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void insertThenRollback() throws RollbackException {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('BBB')");
        throw new RollbackException();
    }
```

### 12 | 了解Spring的JDBC异常抽象

配置 errorcode 的异常为 custom exception

```xml
        <property name="customTranslations">
            <bean class="org.springframework.jdbc.support.CustomSQLErrorCodesTranslation">
                <property name="errorCodes" value="23001,23505" />
                <property name="exceptionClass"
                          value="com.example.errorcodedemo.CustomDuplicatedKeyException" />
            </bean>
        </property>
```

测试抛出异常

```java
    @Test(expected = CustomDuplicatedKeyException.class)
    public void testThrowingCustomException() {
        jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'a')");
        jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'b')");
    }
```

## 第三章：O/R Mapping实践

### 17 | 开始我们的线上咖啡馆实战项目：SpringBucks

配置 Coffee Entity 和它的表 T_MENU。使用 Lombok 配置 Builder、Data 和 Constructor。

```java
@Entity
@Table(name = "T_MENU")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coffee implements Serializable {
```

使用 Joda Money 和 usertype 配置金额和金额转换。注意不要使用浮点型表示金额。

```java
    @Column
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money price;
```

使用 ManyToMany 配置多对多关系。JoinTable 配置外键。

```java
public class CoffeeOrder implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String customer;
    @ManyToMany
    @JoinTable(name = "T_ORDER_COFFEE")
    private List<Coffee> items;
```

定义 CrudRepostory

```java
public interface CoffeeRepository extends CrudRepository<Coffee, Long> {
}
```

保存到数据库

```java
    private void initOrders() {
        Coffee espresso = Coffee.builder()
                .name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
                .build();
        coffeeRepository.save(espresso);
        log.info("Coffee: {}", espresso);
        ...
```
