package com.chokavo.chosportsman.network.cells;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by repitch on 09.03.16.
 */
public class SportObjectCells extends BaseCells {

    @SerializedName("CommonName")
    @Expose
    private String CommonName;
    @SerializedName("RegisterNumber")
    @Expose
    private String RegisterNumber;
    @SerializedName("IncludeDate")
    @Expose
    private String IncludeDate;
    @SerializedName("EGIPRegistryNumber")
    @Expose
    private Object EGIPRegistryNumber;
    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("ShortName")
    @Expose
    private String ShortName;
    @SerializedName("FacilityType")
    @Expose
    private String FacilityType;
    @SerializedName("DepartmentalAffiliation")
    @Expose
    private String DepartmentalAffiliation;
    @SerializedName("AddressOkrug")
    @Expose
    private String AddressOkrug;
    @SerializedName("AddressRegion")
    @Expose
    private String AddressRegion;
    @SerializedName("AddressBti")
    @Expose
    private String AddressBti;
    @SerializedName("AddressBtiId")
    @Expose
    private String AddressBtiId;
    @SerializedName("ChiefName")
    @Expose
    private String ChiefName;
    @SerializedName("HelpPhone")
    @Expose
    private String HelpPhone;
    @SerializedName("HelpPhoneExtension")
    @Expose
    private Object HelpPhoneExtension;
    @SerializedName("WorkingHours")
    @Expose
    private String WorkingHours;
    @SerializedName("ClarificationOfWorkingHours")
    @Expose
    private Object ClarificationOfWorkingHours;
    @SerializedName("Site")
    @Expose
    private Object Site;
    @SerializedName("PropertyType")
    @Expose
    private String PropertyType;
    @SerializedName("Latitude")
    @Expose
    private String Latitude;
    @SerializedName("Longitude")
    @Expose
    private String Longitude;
    @SerializedName("PrisposoblenDlyaInvalidov")
    @Expose
    private String PrisposoblenDlyaInvalidov;
    @SerializedName("GLOBALID")
    @Expose
    private String GLOBALID;

    /**
     * @return The CommonName
     */
    public String getCommonName() {
        return CommonName;
    }

    /**
     * @param CommonName The CommonName
     */
    public void setCommonName(String CommonName) {
        this.CommonName = CommonName;
    }

    /**
     * @return The RegisterNumber
     */
    public String getRegisterNumber() {
        return RegisterNumber;
    }

    /**
     * @param RegisterNumber The RegisterNumber
     */
    public void setRegisterNumber(String RegisterNumber) {
        this.RegisterNumber = RegisterNumber;
    }

    /**
     * @return The IncludeDate
     */
    public String getIncludeDate() {
        return IncludeDate;
    }

    /**
     * @param IncludeDate The IncludeDate
     */
    public void setIncludeDate(String IncludeDate) {
        this.IncludeDate = IncludeDate;
    }

    /**
     * @return The EGIPRegistryNumber
     */
    public Object getEGIPRegistryNumber() {
        return EGIPRegistryNumber;
    }

    /**
     * @param EGIPRegistryNumber The EGIPRegistryNumber
     */
    public void setEGIPRegistryNumber(Object EGIPRegistryNumber) {
        this.EGIPRegistryNumber = EGIPRegistryNumber;
    }

    /**
     * @return The Id
     */
    public String getId() {
        return Id;
    }

    /**
     * @param Id The Id
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     * @return The Name
     */
    public String getName() {
        return Name;
    }

    /**
     * @param Name The Name
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * @return The ShortName
     */
    public String getShortName() {
        return ShortName;
    }

    /**
     * @param ShortName The ShortName
     */
    public void setShortName(String ShortName) {
        this.ShortName = ShortName;
    }

    /**
     * @return The FacilityType
     */
    public String getFacilityType() {
        return FacilityType;
    }

    /**
     * @param FacilityType The FacilityType
     */
    public void setFacilityType(String FacilityType) {
        this.FacilityType = FacilityType;
    }

    /**
     * @return The DepartmentalAffiliation
     */
    public String getDepartmentalAffiliation() {
        return DepartmentalAffiliation;
    }

    /**
     * @param DepartmentalAffiliation The DepartmentalAffiliation
     */
    public void setDepartmentalAffiliation(String DepartmentalAffiliation) {
        this.DepartmentalAffiliation = DepartmentalAffiliation;
    }

    public String getFullAddress() {
        return String.format("%s, %s, %s", getAddressOkrug(), getAddressRegion(), getAddressBti());
    }

    /**
     * @return The AddressOkrug
     */
    public String getAddressOkrug() {
        return AddressOkrug;
    }

    /**
     * @param AddressOkrug The AddressOkrug
     */
    public void setAddressOkrug(String AddressOkrug) {
        this.AddressOkrug = AddressOkrug;
    }

    /**
     * @return The AddressRegion
     */
    public String getAddressRegion() {
        return AddressRegion;
    }

    /**
     * @param AddressRegion The AddressRegion
     */
    public void setAddressRegion(String AddressRegion) {
        this.AddressRegion = AddressRegion;
    }

    /**
     * @return The AddressBti
     */
    public String getAddressBti() {
        return AddressBti;
    }

    /**
     * @param AddressBti The AddressBti
     */
    public void setAddressBti(String AddressBti) {
        this.AddressBti = AddressBti;
    }

    /**
     * @return The AddressBtiId
     */
    public String getAddressBtiId() {
        return AddressBtiId;
    }

    /**
     * @param AddressBtiId The AddressBtiId
     */
    public void setAddressBtiId(String AddressBtiId) {
        this.AddressBtiId = AddressBtiId;
    }

    /**
     * @return The ChiefName
     */
    public String getChiefName() {
        return ChiefName;
    }

    /**
     * @param ChiefName The ChiefName
     */
    public void setChiefName(String ChiefName) {
        this.ChiefName = ChiefName;
    }

    /**
     * @return The HelpPhone
     */
    public String getHelpPhone() {
        return HelpPhone;
    }

    /**
     * @param HelpPhone The HelpPhone
     */
    public void setHelpPhone(String HelpPhone) {
        this.HelpPhone = HelpPhone;
    }

    /**
     * @return The HelpPhoneExtension
     */
    public Object getHelpPhoneExtension() {
        return HelpPhoneExtension;
    }

    /**
     * @param HelpPhoneExtension The HelpPhoneExtension
     */
    public void setHelpPhoneExtension(Object HelpPhoneExtension) {
        this.HelpPhoneExtension = HelpPhoneExtension;
    }

    /**
     * @return The WorkingHours
     */
    public String getWorkingHours() {
        return WorkingHours;
    }

    /**
     * @param WorkingHours The WorkingHours
     */
    public void setWorkingHours(String WorkingHours) {
        this.WorkingHours = WorkingHours;
    }

    /**
     * @return The ClarificationOfWorkingHours
     */
    public Object getClarificationOfWorkingHours() {
        return ClarificationOfWorkingHours;
    }

    /**
     * @param ClarificationOfWorkingHours The ClarificationOfWorkingHours
     */
    public void setClarificationOfWorkingHours(Object ClarificationOfWorkingHours) {
        this.ClarificationOfWorkingHours = ClarificationOfWorkingHours;
    }

    /**
     * @return The Site
     */
    public Object getSite() {
        return Site;
    }

    /**
     * @param Site The Site
     */
    public void setSite(Object Site) {
        this.Site = Site;
    }

    /**
     * @return The PropertyType
     */
    public String getPropertyType() {
        return PropertyType;
    }

    /**
     * @param PropertyType The PropertyType
     */
    public void setPropertyType(String PropertyType) {
        this.PropertyType = PropertyType;
    }

    /**
     * @return The Latitude
     */
    public String getLatitude() {
        return Latitude;
    }

    /**
     * @param Latitude The Latitude
     */
    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    /**
     * @return The Longitude
     */
    public String getLongitude() {
        return Longitude;
    }

    /**
     * @param Longitude The Longitude
     */
    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    /**
     * @return The PrisposoblenDlyaInvalidov
     */
    public String getPrisposoblenDlyaInvalidov() {
        return PrisposoblenDlyaInvalidov;
    }

    /**
     * @param PrisposoblenDlyaInvalidov The PrisposoblenDlyaInvalidov
     */
    public void setPrisposoblenDlyaInvalidov(String PrisposoblenDlyaInvalidov) {
        this.PrisposoblenDlyaInvalidov = PrisposoblenDlyaInvalidov;
    }

    /**
     * @return The GLOBALID
     */
    public String getGLOBALID() {
        return GLOBALID;
    }

    /**
     * @param GLOBALID The GLOBALID
     */
    public void setGLOBALID(String GLOBALID) {
        this.GLOBALID = GLOBALID;
    }

}