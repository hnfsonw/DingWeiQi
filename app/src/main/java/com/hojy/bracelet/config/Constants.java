package com.hojy.bracelet.config;

import android.Manifest;
import android.os.Environment;

import com.hojy.bracelet.base.MyApplication;
import com.hojy.bracelet.util.LogUtils;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 常量配置
 */
public class Constants {

    //测试服务器请求地址
    public static final String TEXTHOST = "https://kidwatch01.hojy.com/nbtracker-api/api/";

    //正式服务器请求地址
    public static final String HOST = "https://kidwatch.hojy.com/hgts/api/";

    //移动招标版本请求地址
    public static final String CMCCHOST = "https://kidwatch01.hojy.com/hgts-cmcc/api/";


    public static final String TEXT = " https://kidwatch-manager.hojy.com/hgts/api/";


    // 网络请求
    /**
     * 1.01 注册（register）
     **/
    public static final String REGISTERNEWACOUNT = "registerNewAcount";

    /**
     * 1.02 登录（acountLogin）
     **/
    public static final String ACOUNTLOGIN = "acountLogin";

    /**
     * 1.03 重置密码（registerNewAcount）
     **/
    public static final String RESETACOUNTPASSWORD = "resetAcountPassword";

    /**
     * 1.04 发送验证码（sendCheckCode）
     **/
    public static final String SENDCHECKCODE = "sendCheckCode";

    /**
     * 1.05 根据用户ID查询app用户信息（queryAcountByName）
     **/
    public static final String QUERYACOUNTBYNAME = "queryAcountByName";

    /**
     * 1.06 根据账户ID查询设备绑定列表（queryBindingListByAcountId）
     **/
    public static final String QUERYBINDINGLISTBYACOUNTID = "queryBindingListByAcountId";

    /**
     * 1.07 绑定设备（bindDevice）
     **/
    public static final String BINDDEVICE = "bindDevice";

    /**
     * 1.08 根据设备Id查询设备是否有管理员绑定（queryDeviceAdminByImei）
     **/
    public static final String QUERYDEVICEADMINBYIMEI = "queryDeviceAdminByImei ";

    /**
     * 1.09 用户解绑设备（unbindDevice）
     **/
    public static final String UNBINDDEVICE = "unbindDevice ";

    /**
     * 1.10 获取设备最新的一条定位信息（getLastLocationInfo）
     **/
    public static final String GETLASTLOCATIONINFO = "getLastLocationInfo ";

    /**
     * 1.11 根据设备Id查询电子围栏集合（queryFenceListByDeviceId）
     **/
    public static final String QUERYFENCELISTBYDEVICEID = "queryFenceListByDeviceId ";

    /**
     * 1.12 新增或修改电子围栏（saveOrUpdateFenceInfo）
     **/
    public static final String SAVEORUPDATEFENCEINFO = "saveOrUpdateFenceInfo ";

    /**
     * 1.13 根据围栏Id删除该条围栏信息（deleteFenceInfoById）
     **/
    public static final String DELETEFENCEINFOBYID = "deleteFenceInfoById ";

    /**
     * 1.14 APP更新极光通讯ID（updateAcountCommuIdByAcountId）
     **/
    public static final String UPDATEACOUNTCOMMUIDBYACOUNTID = "updateAcountCommuIdByAcountId ";

    /**
     * 1.15 根据设备ID查询绑定该设备的用户（queryAcountListByDeviceId）
     **/
    public static final String QUERYACOUNTLISTBYDEVICEID = "queryAcountListByDeviceId ";

    /**
     * 1.16 APP申请绑定设备（applyBindDevice）
     **/
    public static final String APPLYBINDDEVICE = "applyBindDevice ";

    /**
     * 1.17 管理员解绑设备所有成员（adminUnbindAllDevice）
     **/
    public static final String ADMINUNBINDALLDEVICE = "adminUnbindAllDevice ";

    /**
     * 1.18 管理员解绑并移交管理员权限（adminUnbindDevice）
     **/
    public static final String ADMINUNBINDDEVICE = "adminUnbindDevice ";

    /**
     * 1.19 移交管理员（changeDeviceAdmin）
     **/
    public static final String CHANGEDEVICEADMIN = "changeDeviceAdmin ";

    /**
     * 1.20 管理员 同意|拒绝绑定申请（replayApplyBindDevice）
     **/
    public static final String REPLAYAPPLYBINDDEVICE = "replayApplyBindDevice ";

    /**
     * 1.21 检查版本是否为最新版本
     **/
    public static final String CHECKVERSION = "checkVersion";

    /**
     * 1.22 根据查询时间查询历史轨迹信息 (getHistoryLocations)
     **/
    public static final String GETHISTORYLOCATIONS = "getHistoryLocations";




    public static final int State0 = 0;//成功
    public static final int State20002 = 20002;//账户信息失效，请重新登录
    public static final int State80001 = 80001;//成功


    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;
    public static final long PROTOCOL_TIMEOUT = 5 * 60 * 1000;//5分钟
    public static final long PROTOCOL_TIMEOUT_HOURS = 60 * 60 * 1000;//一小时
    public static final long PROTOCOL_TIMEOUT_DAY = 24 * 60 * 60 * 1000;//一天
    public static final long PROTOCOL_TIMEOUT_MONTH = (long) (30L * 24L * 60L * 60L * 1000L);//一个月

    public final static String APP_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + MyApplication.sInstance.getPackageName();
    public final static String DOWNLOAD_DIR = "/downlaod/";
}
