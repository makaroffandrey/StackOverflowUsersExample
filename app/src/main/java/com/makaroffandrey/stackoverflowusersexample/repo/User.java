package com.makaroffandrey.stackoverflowusersexample.repo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    public long account_id;
    public String display_name;
    public long reputation;
    public String location;
    public long creation_date;
    public String profile_image;
}
