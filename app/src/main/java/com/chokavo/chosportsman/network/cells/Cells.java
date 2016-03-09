
package com.chokavo.chosportsman.network.cells;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Cells extends BaseCells {

    @SerializedName("global_id")
    @Expose
    private Integer globalId;
    @SerializedName("NameOfTariff")
    @Expose
    private String NameOfTariff;
    @SerializedName("TypeOfTransport")
    @Expose
    private String TypeOfTransport;
    @SerializedName("NameOfCarrier")
    @Expose
    private List<com.chokavo.chosportsman.network.NameOfCarrier> NameOfCarrier = new ArrayList<com.chokavo.chosportsman.network.NameOfCarrier>();
    @SerializedName("TicketCost")
    @Expose
    private Integer TicketCost;
    @SerializedName("TicketValidity")
    @Expose
    private String TicketValidity;

    /**
     * 
     * @return
     *     The globalId
     */
    public Integer getGlobalId() {
        return globalId;
    }

    /**
     * 
     * @param globalId
     *     The global_id
     */
    public void setGlobalId(Integer globalId) {
        this.globalId = globalId;
    }

    /**
     * 
     * @return
     *     The NameOfTariff
     */
    public String getNameOfTariff() {
        return NameOfTariff;
    }

    /**
     * 
     * @param NameOfTariff
     *     The NameOfTariff
     */
    public void setNameOfTariff(String NameOfTariff) {
        this.NameOfTariff = NameOfTariff;
    }

    /**
     * 
     * @return
     *     The TypeOfTransport
     */
    public String getTypeOfTransport() {
        return TypeOfTransport;
    }

    /**
     * 
     * @param TypeOfTransport
     *     The TypeOfTransport
     */
    public void setTypeOfTransport(String TypeOfTransport) {
        this.TypeOfTransport = TypeOfTransport;
    }

    /**
     * 
     * @return
     *     The NameOfCarrier
     */
    public List<com.chokavo.chosportsman.network.NameOfCarrier> getNameOfCarrier() {
        return NameOfCarrier;
    }

    /**
     * 
     * @param NameOfCarrier
     *     The NameOfCarrier
     */
    public void setNameOfCarrier(List<com.chokavo.chosportsman.network.NameOfCarrier> NameOfCarrier) {
        this.NameOfCarrier = NameOfCarrier;
    }

    /**
     * 
     * @return
     *     The TicketCost
     */
    public Integer getTicketCost() {
        return TicketCost;
    }

    /**
     * 
     * @param TicketCost
     *     The TicketCost
     */
    public void setTicketCost(Integer TicketCost) {
        this.TicketCost = TicketCost;
    }

    /**
     * 
     * @return
     *     The TicketValidity
     */
    public String getTicketValidity() {
        return TicketValidity;
    }

    /**
     * 
     * @param TicketValidity
     *     The TicketValidity
     */
    public void setTicketValidity(String TicketValidity) {
        this.TicketValidity = TicketValidity;
    }

}
