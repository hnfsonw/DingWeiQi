package com.hojy.bracelet.model;

/**
 * Created by LoyBin on 18/3/21  15:41.
 * 描    述:
 */

public class ExtrasModel {

    /**
     * code : 1
     * param : {"acountId":6,"imei":"222222222222222","acountName":"13421836754","deviceId":7}
     * type : 1
     */

    private int code;
    private ParamBean param;
    private int type;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class ParamBean {
        /**
         * acountId : 6
         * imei : 222222222222222
         * acountName : 13421836754
         * deviceId : 7
         */

        private int acountId;
        private String imei;
        private String acountName;
        private int deviceId;
        private String replayStatus;
        private String nickName;

        public String getReplayStatus() {
            return replayStatus;
        }

        public void setReplayStatus(String replayStatus) {
            this.replayStatus = replayStatus;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getAcountId() {
            return acountId;
        }

        public void setAcountId(int acountId) {
            this.acountId = acountId;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getAcountName() {
            return acountName;
        }

        public void setAcountName(String acountName) {
            this.acountName = acountName;
        }

        public int getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(int deviceId) {
            this.deviceId = deviceId;
        }
    }
}
