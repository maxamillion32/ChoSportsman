
package com.chokavo.chosportsman.network.opendata.sportobject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChiefPhone_ {

    @SerializedName("ChiefPhone")
    @Expose
    private String chiefPhone;

    /**
     * 
     * @return
     *     The chiefPhone
     */
    public String getChiefPhone() {
        return chiefPhone;
    }

    /**
     * 
     * @param chiefPhone
     *     The ChiefPhone
     */
    public void setChiefPhone(String chiefPhone) {
        this.chiefPhone = chiefPhone;
    }

}
