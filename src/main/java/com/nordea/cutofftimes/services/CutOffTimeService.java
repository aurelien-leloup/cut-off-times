package com.nordea.cutofftimes.services;

import com.nordea.cutofftimes.daos.CutOffTimeRepository;
import com.nordea.cutofftimes.entities.CutOffTime;
import com.nordea.cutofftimes.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CutOffTimeService {

    @Autowired
    CutOffTimeRepository repository;

    public LocalDateTime getCutOffTime(List<String> currencies) {
        int minutes = currencies.stream()
                .map(currency -> repository
                        .findById(currency)
                        .orElseThrow(() -> new NotFoundException(currency + " is not supported")
                ))
                .map(this::getCutOffTimeMinutes)
                .min(Integer::compareTo)
                .orElseThrow(NoSuchElementException::new);

        return LocalDate.now().atStartOfDay().plusMinutes(minutes);
    }

    private int getCutOffTimeMinutes(CutOffTime cutOffTime) {
        int minutes = 0;

        if (cutOffTime.getToday().equals("Never possible")) {
            minutes += 24 * 60;
        } else {
            return parseTimeToMinutes(cutOffTime.getToday());
        }

        if (cutOffTime.getTomorrow().equals("Never possible")) {
            minutes += 24 * 60;
        } else {
            return minutes + parseTimeToMinutes(cutOffTime.getTomorrow());
        }

        return minutes;
    }

    private int parseTimeToMinutes(String time) {
        String[] timeSplitted = time.split("\\.");
        return Integer.parseInt(timeSplitted[0]) * 60 + Integer.parseInt(timeSplitted[1]);
    }
}
