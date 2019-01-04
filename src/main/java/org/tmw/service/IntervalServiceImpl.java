package org.tmw.service;

import org.springframework.stereotype.Repository;
import org.tmw.model.Interval;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;

@Repository
public class IntervalServiceImpl implements IntervalService {
    @Override
    public List<Interval> getIntervals(String username) {
        return asList( new Interval("Nced", LocalDateTime.now().minusHours(7), LocalDateTime.now()));
    }
}
