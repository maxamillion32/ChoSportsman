
package com.chokavo.chosportsman.network.opendata.sportobject;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjectAddress {

    @SerializedName("AdmArea")
    @Expose
    private String admArea;
    @SerializedName("District")
    @Expose
    private String district;
    @SerializedName("PostalCode")
    @Expose
    private String postalCode;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Availability")
    @Expose
    private List<Object> availability = new ArrayList<Object>();

    /**
     * 
     * @return
     *     The admArea
     */
    public String getAdmArea() {
        return admArea;
    }

    /**
     * 
     * @param admArea
     *     The AdmArea
     */
    public void setAdmArea(String admArea) {
        this.admArea = admArea;
    }

    /**
     * 
     * @return
     *     The district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * 
     * @param district
     *     The District
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * 
     * @return
     *     The postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * 
     * @param postalCode
     *     The PostalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * 
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The Address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * @return
     *     The availability
     */
    public List<Object> getAvailability() {
        return availability;
    }

    /**
     * 
     * @param availability
     *     The Availability
     */
    public void setAvailability(List<Object> availability) {
        this.availability = availability;
    }

}
