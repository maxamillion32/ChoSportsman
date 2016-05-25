
package com.chokavo.chosportsman.network.opendata.sportobject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SportObject {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Number")
    @Expose
    private Integer number;
    @SerializedName("Cells")
    @Expose
    private Cells cells;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The Id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 
     * @param number
     *     The Number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 
     * @return
     *     The cells
     */
    public Cells getCells() {
        return cells;
    }

    /**
     * 
     * @param cells
     *     The Cells
     */
    public void setCells(Cells cells) {
        this.cells = cells;
    }

}
