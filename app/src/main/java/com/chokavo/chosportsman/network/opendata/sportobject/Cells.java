
package com.chokavo.chosportsman.network.opendata.sportobject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cells {

    @SerializedName("global_id")
    @Expose
    private Integer globalId;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("ShortName")
    @Expose
    private String shortName;
    @SerializedName("CommonName")
    @Expose
    private String commonName;
    @SerializedName("OrgInfo")
    @Expose
    private List<OrgInfo> orgInfo = new ArrayList<OrgInfo>();
    @SerializedName("ObjectAddress")
    @Expose
    private List<ObjectAddress> objectAddress = new ArrayList<ObjectAddress>();
    @SerializedName("ChiefName")
    @Expose
    private String chiefName;
    @SerializedName("ChiefPosition")
    @Expose
    private String chiefPosition;
    @SerializedName("ChiefPhone")
    @Expose
    private List<ChiefPhone_> chiefPhone = new ArrayList<ChiefPhone_>();
    @SerializedName("PublicPhone")
    @Expose
    private List<PublicPhone> publicPhone = new ArrayList<PublicPhone>();
    @SerializedName("Fax")
    @Expose
    private List<Object> fax = new ArrayList<Object>();
    @SerializedName("Email")
    @Expose
    private List<Email> email = new ArrayList<Email>();
    @SerializedName("WebSite")
    @Expose
    private Object webSite;
    @SerializedName("WorkingHours")
    @Expose
    private List<WorkingHour> workingHours = new ArrayList<WorkingHour>();
    @SerializedName("ClarificationWorkingHours")
    @Expose
    private Object clarificationWorkingHours;
    @SerializedName("geoData")
    @Expose
    private GeoData geoData;

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
     *     The shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * 
     * @param shortName
     *     The ShortName
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * 
     * @return
     *     The commonName
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * 
     * @param commonName
     *     The CommonName
     */
    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    /**
     * 
     * @return
     *     The orgInfo
     */
    public List<OrgInfo> getOrgInfo() {
        return orgInfo;
    }

    /**
     * 
     * @param orgInfo
     *     The OrgInfo
     */
    public void setOrgInfo(List<OrgInfo> orgInfo) {
        this.orgInfo = orgInfo;
    }

    /**
     * 
     * @return
     *     The objectAddress
     */
    public List<ObjectAddress> getObjectAddress() {
        return objectAddress;
    }

    /**
     * 
     * @param objectAddress
     *     The ObjectAddress
     */
    public void setObjectAddress(List<ObjectAddress> objectAddress) {
        this.objectAddress = objectAddress;
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
    public List<ChiefPhone_> getChiefPhone() {
        return chiefPhone;
    }

    /**
     * 
     * @param chiefPhone
     *     The ChiefPhone
     */
    public void setChiefPhone(List<ChiefPhone_> chiefPhone) {
        this.chiefPhone = chiefPhone;
    }

    /**
     * 
     * @return
     *     The publicPhone
     */
    public List<PublicPhone> getPublicPhone() {
        return publicPhone;
    }

    /**
     * 
     * @param publicPhone
     *     The PublicPhone
     */
    public void setPublicPhone(List<PublicPhone> publicPhone) {
        this.publicPhone = publicPhone;
    }

    /**
     * 
     * @return
     *     The fax
     */
    public List<Object> getFax() {
        return fax;
    }

    /**
     * 
     * @param fax
     *     The Fax
     */
    public void setFax(List<Object> fax) {
        this.fax = fax;
    }

    /**
     * 
     * @return
     *     The email
     */
    public List<Email> getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     *     The Email
     */
    public void setEmail(List<Email> email) {
        this.email = email;
    }

    /**
     * 
     * @return
     *     The webSite
     */
    public Object getWebSite() {
        return webSite;
    }

    /**
     * 
     * @param webSite
     *     The WebSite
     */
    public void setWebSite(Object webSite) {
        this.webSite = webSite;
    }

    /**
     * 
     * @return
     *     The workingHours
     */
    public List<WorkingHour> getWorkingHours() {
        return workingHours;
    }

    /**
     * 
     * @param workingHours
     *     The WorkingHours
     */
    public void setWorkingHours(List<WorkingHour> workingHours) {
        this.workingHours = workingHours;
    }

    /**
     * 
     * @return
     *     The clarificationWorkingHours
     */
    public Object getClarificationWorkingHours() {
        return clarificationWorkingHours;
    }

    /**
     * 
     * @param clarificationWorkingHours
     *     The ClarificationWorkingHours
     */
    public void setClarificationWorkingHours(Object clarificationWorkingHours) {
        this.clarificationWorkingHours = clarificationWorkingHours;
    }

    /**
     * 
     * @return
     *     The geoData
     */
    public GeoData getGeoData() {
        return geoData;
    }

    /**
     * 
     * @param geoData
     *     The geoData
     */
    public void setGeoData(GeoData geoData) {
        this.geoData = geoData;
    }

    private static final String TODAY_NOT_WORK = "Сегодня не работает";

    public String getReadableWorkingHours() {
        Calendar now = Calendar.getInstance();
        int dayWeek = (now.get(Calendar.DAY_OF_WEEK)+6) % 7;
        WorkingHour wh = getWorkingHours().get(dayWeek);
        String whStr = wh.getWorkHours();
        if (whStr == null || whStr.isEmpty()) {
            return TODAY_NOT_WORK;
        }
        String[] hours = whStr.split("-");
        if (hours.length != 2) {
            return TODAY_NOT_WORK;
        }
        int nowMinsOfDay = now.get(Calendar.MINUTE) + now.get(Calendar.HOUR_OF_DAY)*60;
        String[] start = hours[0].split(":");
        String[] end = hours[1].split(":");
        int startMins = Integer.parseInt(start[0])*60+Integer.parseInt(start[1]);
        int endMins = Integer.parseInt(end[0])*60+Integer.parseInt(end[1]);
        if (nowMinsOfDay < startMins) {
            return "Работает "+whStr;
        } else if (nowMinsOfDay > endMins) {
            return "Сейчас закрыто";
        } else {
            return "Сейчас работает до "+hours[1];
        }
    }

    public Double getLongitude() {
        List<List<Double>> coords = getGeoData().getCoordinates();
        if (coords == null || coords.isEmpty()) {
            return null;
        }
        return coords.get(0).get(0);
    }

    public String getLonStr() {
        return getLongitude().toString();
    }

    public Double getLatitude() {
        List<List<Double>> coords = getGeoData().getCoordinates();
        if (coords == null || coords.isEmpty()) {
            return null;
        }
        return coords.get(0).get(1);
    }

    public String getLatStr() {
        return getLatitude().toString();
    }

    public String getFirstPublicPhone() {
        List<PublicPhone> phones = getPublicPhone();
        if (phones == null || phones.isEmpty()) {
            return "";
        }
        return phones.get(0).getPublicPhone();
    }
}
