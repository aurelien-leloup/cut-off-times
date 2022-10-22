package com.nordea.cutofftimes.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class CutOffTime {

    @Id
    private String iso;

    private String country;

    private String today;

    private String tomorrow;

    private String afterTomorrow;

}
