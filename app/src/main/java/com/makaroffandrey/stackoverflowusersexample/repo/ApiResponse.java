package com.makaroffandrey.stackoverflowusersexample.repo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class ApiResponse<T> {
    public List<T> items;
    public String error_name;
    public String error_message;
}
