
package com.chokavo.chosportsman.network.datarows;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class BaseDataRow {

    @SerializedName("Id")
    @Expose
    protected String Id;
    @SerializedName("Number")
    @Expose
    protected Integer Number;

    /**
     *
     * @return
     *     The Id
     */
    public String getId() {
        return Id;
    }

    /**
     *
     * @param Id
     *     The Id
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     *
     * @return
     *     The Number
     */
    public Integer getNumber() {
        return Number;
    }

    /**
     *
     * @param Number
     *     The Number
     */
    public void setNumber(Integer Number) {
        this.Number = Number;
    }

}
