package com.kodilla.ecommercee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.GeneratedValue;

@Getter
@AllArgsConstructor
public class GroupDto {

    private Long Id;
    private String groupName;
}
