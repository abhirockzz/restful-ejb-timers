package easy.scheduling.audit.model;

import javax.ejb.ScheduleExpression;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ScheduledTimerInfo {

    private String timerID;
    @XmlElement(name = "scheduleConfiguration")
    private ScheduleConfiguration originalConfiguration;
    private long timeRemaining;
    private String nextTrigger;

    public ScheduledTimerInfo() {
    }

    public ScheduledTimerInfo(String timerID, ScheduleExpression expression, long timeRemaining, String nextTrigger) {
        this.timerID = timerID;
        this.originalConfiguration = new ScheduleConfiguration(expression);
        this.timeRemaining = timeRemaining;
        this.nextTrigger = nextTrigger;
    }

}
