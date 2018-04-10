package com.hojy.bracelet.http;

import com.hojy.bracelet.config.Constants;
import com.hojy.bracelet.model.ResponseInfoModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by LoyBin on 18/2/27  11:49.
 * 描    述: 所有网络请求服务 post
 */

public interface WearService {

    /**
     * 注册账号
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.REGISTERNEWACOUNT)
    Call<ResponseInfoModel> register(@FieldMap Map<String, String> fields);


    /**
     * 登录
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.ACOUNTLOGIN)
    Call<ResponseInfoModel> acountLogin(@FieldMap Map<String, String> fields);


    /**
     * 重置密码
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.RESETACOUNTPASSWORD)
    Call<ResponseInfoModel> resetAcountPassword(@FieldMap Map<String, String> fields);


    /**
     * 发送验证码
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.SENDCHECKCODE)
    Call<ResponseInfoModel> sendCheckCode(@FieldMap Map<String, String> fields);


    /**
     * 根据用户ID查询app用户信息
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYACOUNTBYNAME)
    Call<ResponseInfoModel> queryAcountByName(@FieldMap Map<String, String> fields);


    /**
     * 根据账户ID查询设备绑定列表
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYBINDINGLISTBYACOUNTID)
    Call<ResponseInfoModel> queryBindingListByAcountId(@FieldMap Map<String, String> fields);


    /**
     * 根据账户ID查询设备绑定列表
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.BINDDEVICE)
    Call<ResponseInfoModel> bindDevice(@FieldMap Map<String, Object> fields);


    /**
     * 根据设备Id查询设备是否有管理员绑定
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYDEVICEADMINBYIMEI)
    Call<ResponseInfoModel> queryDeviceAdminByImei(@FieldMap Map<String, Object> fields);


    /**
     * 用户解绑设备
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.UNBINDDEVICE)
    Call<ResponseInfoModel> unbindDevice(@FieldMap Map<String, Object> fields);


    /**
     * 获取设备最新的一条定位信息
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.GETLASTLOCATIONINFO)
    Call<ResponseInfoModel> getLastLocationInfo(@FieldMap Map<String, Object> fields);


    /**
     * 根据设备Id查询电子围栏集合
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYFENCELISTBYDEVICEID)
    Call<ResponseInfoModel> queryFenceListByDeviceId(@FieldMap Map<String, Object> fields);


    /**
     * 新增或修改电子围栏
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.SAVEORUPDATEFENCEINFO)
    Call<ResponseInfoModel> saveOrUpdateFenceInfo(@FieldMap Map<String, Object> fields);


    /**
     * 根据围栏Id删除该条围栏信息
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DELETEFENCEINFOBYID)
    Call<ResponseInfoModel> deleteFenceInfoById(@FieldMap Map<String, Object> fields);


    /**
     * APP更新极光通讯ID
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.UPDATEACOUNTCOMMUIDBYACOUNTID)
    Call<ResponseInfoModel> updateAcountCommuIdByAcountId(@FieldMap Map<String, Object> fields);


    /**
     * 根据设备ID查询绑定该设备的用户
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYACOUNTLISTBYDEVICEID)
    Call<ResponseInfoModel> queryAcountListByDeviceId(@FieldMap Map<String, Object> fields);


    /**
     * APP申请绑定设备
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.APPLYBINDDEVICE)
    Call<ResponseInfoModel> applyBindDevice(@FieldMap Map<String, Object> fields);


    /**
     * 管理员解绑设备所有成员
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.ADMINUNBINDALLDEVICE)
    Call<ResponseInfoModel> adminUnbindAllDevice(@FieldMap Map<String, Object> fields);


    /**
     * 管理员解绑并移交管理员权限
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.ADMINUNBINDDEVICE)
    Call<ResponseInfoModel> adminUnbindDevice(@FieldMap Map<String, Object> fields);


    /**
     * 移交管理员
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.CHANGEDEVICEADMIN)
    Call<ResponseInfoModel> changeDeviceAdmin(@FieldMap Map<String, Object> fields);


    /**
     * 管理员 同意|拒绝绑定申请
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.REPLAYAPPLYBINDDEVICE)
    Call<ResponseInfoModel> replayApplyBindDevice(@FieldMap Map<String, Object> fields);


    /**
     * 检查版本是否为最新版本
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.CHECKVERSION)
    Call<ResponseInfoModel> checkVersion(@FieldMap Map<String, Object> fields);


    /**
     * 检查版本是否为最新版本
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.GETHISTORYLOCATIONS)
    Call<ResponseInfoModel> getHistoryLocations(@FieldMap Map<String, Object> fields);

}
