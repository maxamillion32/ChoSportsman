
package com.chokavo.opendatamos.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataRow {

    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("Number")
    @Expose
    private Integer Number;
    @SerializedName("Cells")
    @Expose
    private com.chokavo.opendatamos.network.Cells Cells;

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

    /**
     * 
     * @return
     *     The Cells
     */
    public com.chokavo.opendatamos.network.Cells getCells() {
        return Cells;
    }

    /**
     * 
     * @param Cells
     *     The Cells
     */
    public void setCells(com.chokavo.opendatamos.network.Cells Cells) {
        this.Cells = Cells;
    }

}
