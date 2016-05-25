
package com.chokavo.chosportsman.network.opendata.sportobject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkingHour {

    @SerializedName("DayWeek")
    @Expose
    private String dayWeek;
    @SerializedName("WorkHours")
    @Expose
    private String workHours;

    /**
     * 
     * @return
     *     The dayWeek
     */
    public String getDayWeek() {
        return dayWeek;
    }

    /**
     * 
     * @param dayWeek
     *     The DayWeek
     */
    public void setDayWeek(String dayWeek) {
        this.dayWeek = dayWeek;
    }

    /**
     * 
     * @return
     *     The workHours
     */
    public String getWorkHours() {
        return workHours;
    }

    /**
     * 
     * @param workHours
     *     The WorkHours
     */
    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

}
