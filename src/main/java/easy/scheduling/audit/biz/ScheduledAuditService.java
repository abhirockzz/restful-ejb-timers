
package easy.scheduling.audit.biz;

import easy.scheduling.audit.model.ScheduledTimerInfo;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

@Stateless
public class ScheduledAuditService {
    
    @Resource
    private TimerService tsvc;
        
    public void schedule(ScheduleExpression expression, TimerConfig timerConfig){
       boolean timerExists = tsvc.getTimers().stream()
                .anyMatch((timer) -> ((String)timer.getInfo()).equals(timerConfig.getInfo()));
       
       if(timerExists){
          throw new IllegalArgumentException("Timer '"+timerConfig.getInfo() +"' already exists!"); 
       }
       tsvc.createCalendarTimer(expression, timerConfig);
    }

    
    public ScheduledTimerInfo getTimerInfo(String name){
        
        boolean timerExists = tsvc.getTimers().stream()
                .anyMatch((timer) -> ((String)timer.getInfo()).equals(name));
        
        if(!timerExists){
            throw new IllegalStateException("Timer '"+name +"' does not exist");
        }
        
         return tsvc.getTimers().stream()
                .filter((timer) -> ((String)timer.getInfo()).equals(name))
                .map((timer) -> new ScheduledTimerInfo(name, timer.getSchedule(), timer.getTimeRemaining(), timer.getNextTimeout().toString()))
                .findFirst().get();
         
    }
  
    public List<String> getAllTimers(){
        return tsvc.getTimers().stream().map((timer) -> ((String)timer.getInfo())).collect(Collectors.toList());
    }

    
    public void cancel(String name){
        
        boolean timerExists = tsvc.getTimers().stream()
                .anyMatch((timer) -> ((String)timer.getInfo()).equals(name));
        
        if(!timerExists){
            throw new IllegalStateException("Timer '"+name +"' does not exist");
        }
        
        tsvc.getTimers().stream()
                .filter((timer) -> ((String)timer.getInfo()).equals(name))
                .findFirst().get()
                .cancel();
    }
    
    @Timeout
    public void execute(Timer timer){
        
        System.out.println("Timer details\n");
        System.out.println("Timer next timeout "+ timer.getNextTimeout());
        System.out.println("Timer persistent ? "+ timer.isPersistent());
    }

  
}
