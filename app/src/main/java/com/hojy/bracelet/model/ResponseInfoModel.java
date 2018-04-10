package com.hojy.bracelet.model;

import java.io.Serializable;
import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/05 下午4:17
 * 描   述: 登入 注册 忘记密码的返回参数
 */
public class ResponseInfoModel implements Serializable {


    /**
     * msg : Success
     * code : 0
     * data : {"acountEntity":{"acountId":5,"acountName":"17666103375","createDate":"2018-03-07 11:21:02","password":"3301bb0804d0a5c7f004cf05b8e4c372aa78986f9dea8267e6aca9d619ec4ed6"}}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * acountEntity : {"acountId":5,"acountName":"17666103375","createDate":"2018-03-07 11:21:02","password":"3301bb0804d0a5c7f004cf05b8e4c372aa78986f9dea8267e6aca9d619ec4ed6"}
         *//**
         * acountName : string,账户名（手机号）
         * token : string,token唯一标识
         */

        private String acountName;
        private String token;
        private long acountId;
        private boolean result;
        private int hasBind;
        private long id;
        private int deviceId;
        private double lng;
        private double lat;
        private double accuracy;
        private int locationType;
        private String address;
        private int mcc;
        private int mnc;
        private String lbsData;
        private String wifiData;
        private int type;
        private String locationTime;
        private String radioType;
        private String carrier;
        private String createDate;
        private String commuId;
        private List<FenceListBean> fenceList;
        private List<AcountListBean> acountList;
        public List<LocationListBean> locationList;

        private boolean hasNewVesion;
        private int versionCode;
        private String version;
        private String url;
        private String desc;
        private String updateTime;

        public List<LocationListBean> getLocationList() {
            return locationList;
        }

        public void setLocationList(List<LocationListBean> locationList) {
            this.locationList = locationList;
        }

        public boolean isHasNewVesion() {
            return hasNewVesion;
        }

        public void setHasNewVesion(boolean hasNewVesion) {
            this.hasNewVesion = hasNewVesion;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public List<AcountListBean> getAcountList() {
            return acountList;
        }

        public void setAcountList(List<AcountListBean> acountList) {
            this.acountList = acountList;
        }

        public String getCommuId() {
            return commuId;
        }

        public void setCommuId(String commuId) {
            this.commuId = commuId;
        }

        public List<FenceListBean> getFenceList() {
            return fenceList;
        }

        public void setFenceList(List<FenceListBean> fenceList) {
            this.fenceList = fenceList;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(int deviceId) {
            this.deviceId = deviceId;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(double accuracy) {
            this.accuracy = accuracy;
        }

        public int getLocationType() {
            return locationType;
        }

        public void setLocationType(int locationType) {
            this.locationType = locationType;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getMcc() {
            return mcc;
        }

        public void setMcc(int mcc) {
            this.mcc = mcc;
        }

        public int getMnc() {
            return mnc;
        }

        public void setMnc(int mnc) {
            this.mnc = mnc;
        }

        public String getLbsData() {
            return lbsData;
        }

        public void setLbsData(String lbsData) {
            this.lbsData = lbsData;
        }

        public String getWifiData() {
            return wifiData;
        }

        public void setWifiData(String wifiData) {
            this.wifiData = wifiData;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getLocationTime() {
            return locationTime;
        }

        public void setLocationTime(String locationTime) {
            this.locationTime = locationTime;
        }

        public String getRadioType() {
            return radioType;
        }

        public void setRadioType(String radioType) {
            this.radioType = radioType;
        }

        public String getCarrier() {
            return carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public int getHasBind() {
            return hasBind;
        }

        public void setHasBind(int hasBind) {
            this.hasBind = hasBind;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        private List<BindingListBean> bindingList;

        public List<BindingListBean> getBindingList() {
            return bindingList;
        }

        public void setBindingList(List<BindingListBean>  bindingList) {
            this.bindingList = bindingList;
        }

        public long getAcountId() {
            return acountId;
        }

        public void setAcountId(long acountId) {
            this.acountId = acountId;
        }



        public String getAcountName() {
            return acountName;
        }

        public void setAcountName(String acountName) {
            this.acountName = acountName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public static class AcountEntityBean {
            /**
             * acountId : 5
             * acountName : 17666103375
             * createDate : 2018-03-07 11:21:02
             * password : 3301bb0804d0a5c7f004cf05b8e4c372aa78986f9dea8267e6aca9d619ec4ed6
             */

            private int acountId;
            private String acountName;
            private String createDate;
            private String password;

            public int getAcountId() {
                return acountId;
            }

            public void setAcountId(int acountId) {
                this.acountId = acountId;
            }

            public String getAcountName() {
                return acountName;
            }

            public void setAcountName(String acountName) {
                this.acountName = acountName;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }
        }

        public static class BindingListBean implements Serializable{
            /**
             * address : null
             * age : null
             * commuId : ffac765e-eff3-4811-a3e8-98a7646691b5
             * commuType : 1
             * createDate : 2018-03-07 10:45:08
             * deviceId : 2
             * deviceType : KEY
             * hardVersion : null
             * height : null
             * imei : 987654321234567
             * imgUrl : null
             * nickName : null
             * phone : null
             * secret : 532077b0caef46592e233e863dc68242
             * sex : null
             * softVersion : null
             * type : 2
             * userName : null
             * weight : null
             */

            private Object address;
            private Object age;
            private String commuId;
            private int commuType;
            private String createDate;
            private int deviceId;
            private String deviceType;
            private Object hardVersion;
            private Object height;
            private String imei;
            private Object imgUrl;
            private String nickName;
            private Object phone;
            private String secret;
            private Object sex;
            private Object softVersion;
            private int type;
            private Object userName;
            private Object weight;
            private int isAdmin;

            public int isAdmin() {
                return isAdmin;
            }

            public void setAdmin(int admin) {
                isAdmin = admin;
            }

            public Object getAddress() {
                return address;
            }

            public void setAddress(Object address) {
                this.address = address;
            }

            public Object getAge() {
                return age;
            }

            public void setAge(Object age) {
                this.age = age;
            }

            public String getCommuId() {
                return commuId;
            }

            public void setCommuId(String commuId) {
                this.commuId = commuId;
            }

            public int getCommuType() {
                return commuType;
            }

            public void setCommuType(int commuType) {
                this.commuType = commuType;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public int getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(int deviceId) {
                this.deviceId = deviceId;
            }

            public String getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(String deviceType) {
                this.deviceType = deviceType;
            }

            public Object getHardVersion() {
                return hardVersion;
            }

            public void setHardVersion(Object hardVersion) {
                this.hardVersion = hardVersion;
            }

            public Object getHeight() {
                return height;
            }

            public void setHeight(Object height) {
                this.height = height;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public Object getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(Object imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public Object getPhone() {
                return phone;
            }

            public void setPhone(Object phone) {
                this.phone = phone;
            }

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public Object getSex() {
                return sex;
            }

            public void setSex(Object sex) {
                this.sex = sex;
            }

            public Object getSoftVersion() {
                return softVersion;
            }

            public void setSoftVersion(Object softVersion) {
                this.softVersion = softVersion;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public Object getUserName() {
                return userName;
            }

            public void setUserName(Object userName) {
                this.userName = userName;
            }

            public Object getWeight() {
                return weight;
            }

            public void setWeight(Object weight) {
                this.weight = weight;
            }
        }

        public static class FenceListBean {
            /**
             * alarmType : 1
             * createDate : 2018-03-13 16:52:49
             * desc : null
             * deviceId : 7
             * endTime : 36000000
             * endTimeStr : 18:00
             * fenceType : 1
             * id : 10
             * lat : 24.65
             * lng : 118.53
             * name : 用户绑定模块
             * positionState : 0
             * radius : 200
             * startTime : 7200000
             * startTimeStr : 10:00
             * state : 1
             */

            private int alarmType;
            private String createDate;
            private String desc;
            private int deviceId;
            private int endTime;
            private String endTimeStr;
            private int fenceType;
            public int id;
            private double lat;
            private double lng;
            private String name;
            private int positionState;
            private int radius;
            private int startTime;
            private String startTimeStr;
            public int state;

            public int getAlarmType() {
                return alarmType;
            }

            public void setAlarmType(int alarmType) {
                this.alarmType = alarmType;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(int deviceId) {
                this.deviceId = deviceId;
            }

            public int getEndTime() {
                return endTime;
            }

            public void setEndTime(int endTime) {
                this.endTime = endTime;
            }

            public String getEndTimeStr() {
                return endTimeStr;
            }

            public void setEndTimeStr(String endTimeStr) {
                this.endTimeStr = endTimeStr;
            }

            public int getFenceType() {
                return fenceType;
            }

            public void setFenceType(int fenceType) {
                this.fenceType = fenceType;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPositionState() {
                return positionState;
            }

            public void setPositionState(int positionState) {
                this.positionState = positionState;
            }

            public int getRadius() {
                return radius;
            }

            public void setRadius(int radius) {
                this.radius = radius;
            }

            public int getStartTime() {
                return startTime;
            }

            public void setStartTime(int startTime) {
                this.startTime = startTime;
            }

            public String getStartTimeStr() {
                return startTimeStr;
            }

            public void setStartTimeStr(String startTimeStr) {
                this.startTimeStr = startTimeStr;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }
        }

        public static class AcountListBean {
            /**
             * acountId : 5
             * isAdmin : 1
             * acountName : 17666103375
             */

            private int acountId;
            private int isAdmin;
            private String acountName;

            public int getAcountId() {
                return acountId;
            }

            public void setAcountId(int acountId) {
                this.acountId = acountId;
            }

            public int getIsAdmin() {
                return isAdmin;
            }

            public void setIsAdmin(int isAdmin) {
                this.isAdmin = isAdmin;
            }

            public String getAcountName() {
                return acountName;
            }

            public void setAcountName(String acountName) {
                this.acountName = acountName;
            }
        }

        public static class LocationListBean implements Serializable {
            private int id;
            private String lbsData;
            private int mcc;
            private String wifiData;
            private int mnc;
            private double lng;
            private int type;
            private String locationTime;
            private double lat;
            private int deviceId;
            private String address;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLbsData() {
                return lbsData;
            }

            public void setLbsData(String lbsData) {
                this.lbsData = lbsData;
            }

            public int getMcc() {
                return mcc;
            }

            public void setMcc(int mcc) {
                this.mcc = mcc;
            }

            public String getWifiData() {
                return wifiData;
            }

            public void setWifiData(String wifiData) {
                this.wifiData = wifiData;
            }

            public int getMnc() {
                return mnc;
            }

            public void setMnc(int mnc) {
                this.mnc = mnc;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getLocationTime() {
                return locationTime;
            }

            public void setLocationTime(String locationTime) {
                this.locationTime = locationTime;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public int getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(int deviceId) {
                this.deviceId = deviceId;
            }
        }
    }
}
