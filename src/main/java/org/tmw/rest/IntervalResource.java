package org.tmw.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.tmw.model.Interval;
import org.tmw.service.IntervalService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;

@Path("interval")
@Produces("application/json")
public class IntervalResource extends SpringAwareResource {

    @Autowired
    private IntervalService intervalService;

    @GET
    @Path("/{username}")
    public List<Interval> getSampleIntervals(@PathParam("username") String username) {
        return intervalService.getIntervals(username);
    }
}
