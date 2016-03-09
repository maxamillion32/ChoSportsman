
package com.chokavo.chosportsman.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NameOfCarrier {

    @SerializedName("NameOfCarrier")
    @Expose
    private String NameOfCarrier;

    /**
     * 
     * @return
     *     The NameOfCarrier
     */
    public String getNameOfCarrier() {
        return NameOfCarrier;
    }

    /**
     * 
     * @param NameOfCarrier
     *     The NameOfCarrier
     */
    public void setNameOfCarrier(String NameOfCarrier) {
        this.NameOfCarrier = NameOfCarrier;
    }

}
