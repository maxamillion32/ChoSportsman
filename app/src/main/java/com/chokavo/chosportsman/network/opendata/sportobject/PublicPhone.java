
package com.chokavo.chosportsman.network.opendata.sportobject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PublicPhone {

    @SerializedName("PublicPhone")
    @Expose
    private String publicPhone;

    /**
     * 
     * @return
     *     The publicPhone
     */
    public String getPublicPhone() {
        return publicPhone;
    }

    /**
     * 
     * @param publicPhone
     *     The PublicPhone
     */
    public void setPublicPhone(String publicPhone) {
        this.publicPhone = publicPhone;
    }

}
