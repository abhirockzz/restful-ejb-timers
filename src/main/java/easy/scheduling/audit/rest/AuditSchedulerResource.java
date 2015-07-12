
package easy.scheduling.audit.rest;

import easy.scheduling.audit.biz.ScheduledAuditService;
import easy.scheduling.audit.model.ScheduleConfiguration;
import easy.scheduling.audit.model.ScheduledTimerInfo;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.ScheduleExpression;
import javax.ejb.TimerConfig;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Path("audit")
public class AuditSchedulerResource {
    
    @Inject
    ScheduledAuditService auditScheduler;
    
    /**
     * Schedule a new timer
     * 
     * @param timerName unique name for the timer
     * @param config timer configuration details
     * @return 
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response schedule(@HeaderParam("name") final String timerName, final ScheduleConfiguration config) {
        
        auditScheduler.schedule(from(config), new TimerConfig(timerName, config.isPersistent()));
        System.out.println("Scheduled Timer -- "+ timerName);

        return Response.created(UriBuilder.fromResource(AuditSchedulerResource.class).path(timerName).build(timerName)).build();
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getTimerInfo(@PathParam("id") String name){
        ScheduledTimerInfo info = auditScheduler.getTimerInfo(name);
        System.out.println("Returning information about timer -- "+ name);
        return Response.ok(info).build();
    }
    
    /**
     * 
     * @return 
     */
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAllTimers(){
        List<String> ejbTimers = auditScheduler.getAllTimers();
        List<ScheduledTimerInfo> timers = ejbTimers.stream().map((id) -> auditScheduler.getTimerInfo(id)).collect(Collectors.toList());
        
        GenericEntity<List<ScheduledTimerInfo>> entities = new GenericEntity<List<ScheduledTimerInfo>>(timers) {};
        System.out.println("Returning all active timers in the system");    
        return Response.ok(entities).build();
    }

    /**
     * 
     * @param name 
     */
    
    @DELETE
    @Path("{id}")
    public void cancel(@PathParam("id") String name){
        auditScheduler.cancel(name);
        System.out.println("Timer '" + name + "' cancelled");
    }
    
    private static ScheduleExpression from(ScheduleConfiguration config){
        return new ScheduleExpression()
                            .second(config.getSecond())
                            .minute(config.getMinute())
                            .hour(config.getHour())
                            .dayOfWeek(config.getDayOfWeek())
                            .dayOfMonth(config.getDayOfMonth())
                            .month(config.getMonth())
                            .year(config.getYear());
    }
}
