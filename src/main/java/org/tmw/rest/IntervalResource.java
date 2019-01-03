package org.tmw.rest;

import org.tmw.model.Interval;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;

@Path("interval")
@Produces("application/json")
public class IntervalResource {
    @GET
    public List<Interval> getSampleIntervals() {
        return asList( new Interval("user1", LocalDateTime.now().minusHours(2), LocalDateTime.now()));
    }
}
