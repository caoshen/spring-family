package com.example.simplejdbcdemo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Foo {
    private long id;
    private String bar;

    public Foo(long id, String bar) {
        this.id = id;
        this.bar = bar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }
}
