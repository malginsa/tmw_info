package org.tmw.service;

import org.tmw.model.Interval;

import java.util.List;

public interface IntervalService {
    List<Interval> getIntervals(String username);
}
