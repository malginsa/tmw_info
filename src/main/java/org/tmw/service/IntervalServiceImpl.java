package org.tmw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.tmw.DAO;
import org.tmw.model.Interval;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;

@Service
public class IntervalServiceImpl implements IntervalService {

    @Autowired
    private DAO dao;

    @Override
    public List<Interval> getIntervals(String username) {
//        return asList( new Interval("Nced", LocalDateTime.now().minusHours(7), LocalDateTime.now()));
        return asList( new Interval("Nced", LocalDateTime.now().minusHours(7), LocalDateTime.now()));
    }
}
