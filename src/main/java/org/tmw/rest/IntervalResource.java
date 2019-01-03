package org.tmw.rest;

import org.tmw.model.Interval;
import org.tmw.service.IntervalService;
import org.tmw.service.IntervalServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;

@Path("interval")
@Produces("application/json")
public class IntervalResource {

    private IntervalService intervalService = new IntervalServiceImpl();

    @GET
    public List<Interval> getSampleIntervals() {
        return asList( new Interval("user1", LocalDateTime.now().minusHours(2), LocalDateTime.now()));
    }

    @GET
    @Path("Nced")
    public List<Interval> getNcedIntervals() {
        return intervalService.getIntervals("Nced");
    }
}
