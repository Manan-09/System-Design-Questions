package model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Schedule {
    DayOfWeek weekDay;
    LocalTime startTime;
    LocalTime endTime;

    public Schedule(DayOfWeek weekDay, LocalTime startTime, LocalTime endTime) {
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DayOfWeek getWeekDay() {
        return weekDay;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
