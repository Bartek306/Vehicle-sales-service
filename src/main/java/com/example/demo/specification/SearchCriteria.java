package com.example.demo.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;

}
