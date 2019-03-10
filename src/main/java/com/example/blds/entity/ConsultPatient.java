package com.example.blds.entity;

import lombok.Data;

import javax.persistence.Id;

@Data
public class ConsultPatient {
    @Id
    private Integer id;

    private Integer consultId;

    private String patientName;

    private String sex;

    private Integer age;

    private Integer mzNum;


}