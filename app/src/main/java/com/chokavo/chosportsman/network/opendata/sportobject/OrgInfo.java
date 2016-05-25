
package com.chokavo.chosportsman.network.opendata.sportobject;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrgInfo {

    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("INN")
    @Expose
    private String iNN;
    @SerializedName("KPP")
    @Expose
    private String kPP;
    @SerializedName("OGRN")
    @Expose
    private String oGRN;
    @SerializedName("LegalAddress")
    @Expose
    private String legalAddress;
    @SerializedName("ChiefName")
    @Expose
    private String chiefName;
    @SerializedName("ChiefPosition")
    @Expose
    private String chiefPosition;
    @SerializedName("ChiefPhone")
    @Expose
    private List<ChiefPhone> chiefPhone = new ArrayList<ChiefPhone>();

    /**
     * 
     * @return
     *     The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 
     * @param fullName
     *     The FullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 
     * @return
     *     The iNN
     */
    public String getINN() {
        return iNN;
    }

    /**
     * 
     * @param iNN
     *     The INN
     */
    public void setINN(String iNN) {
        this.iNN = iNN;
    }

    /**
     * 
     * @return
     *     The kPP
     */
    public String getKPP() {
        return kPP;
    }

    /**
     * 
     * @param kPP
     *     The KPP
     */
    public void setKPP(String kPP) {
        this.kPP = kPP;
    }

    /**
     * 
     * @return
     *     The oGRN
     */
    public String getOGRN() {
        return oGRN;
    }

    /**
     * 
     * @param oGRN
     *     The OGRN
     */
    public void setOGRN(String oGRN) {
        this.oGRN = oGRN;
    }

    /**
     * 
     * @return
     *     The legalAddress
     */
    public String getLegalAddress() {
        return legalAddress;
    }

    /**
     * 
     * @param legalAddress
     *     The LegalAddress
     */
    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    /**
     * 
     * @return
     *     The chiefName
     */
    public String getChiefName() {
        return chiefName;
    }

    /**
     * 
     * @param chiefName
     *     The ChiefName
     */
    public void setChiefName(String chiefName) {
        this.chiefName = chiefName;
    }

    /**
     * 
     * @return
     *     The chiefPosition
     */
    public String getChiefPosition() {
        return chiefPosition;
    }

    /**
     * 
     * @param chiefPosition
     *     The ChiefPosition
     */
    public void setChiefPosition(String chiefPosition) {
        this.chiefPosition = chiefPosition;
    }

    /**
     * 
     * @return
     *     The chiefPhone
     */
    public List<ChiefPhone> getChiefPhone() {
        return chiefPhone;
    }

    /**
     * 
     * @param chiefPhone
     *     The ChiefPhone
     */
    public void setChiefPhone(List<ChiefPhone> chiefPhone) {
        this.chiefPhone = chiefPhone;
    }

}
