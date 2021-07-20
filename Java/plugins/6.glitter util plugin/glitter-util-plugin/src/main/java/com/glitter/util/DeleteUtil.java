package com.glitter.util;

import com.alibaba.fastjson.JSONObject;
import com.glitter.dto.LoginUserDTO;
import com.glitter.enums.CommonEnums;
import com.glitter.threadlocal.LoginUserContext;
import com.glitter.threadlocal.RequestTimeContext;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * 删除工具方法
 *
 * @author limengjun
 * @date 2020/5/23 10:51
 **/
@Log4j2
public class DeleteUtil {
    public static <T> T getDeleteDO(Class<T> clazz, Integer id) throws Exception {
        log.info("DeleteUtil.getDeleteDO,输入参数,clazz:{}, map:{}", clazz, id);
        LoginUserDTO loginUserDTO = null;
        try {
            loginUserDTO = LoginUserContext.get();
        } catch (Exception e) {
            log.error("DeleteUtil.getDeleteDO,异常信息:{}", JSONObject.toJSONString(e));
        }

        T t = clazz.newInstance();
        Method setIdMethod = t.getClass().getMethod("setId", Integer.class);
        setIdMethod.invoke(t, id);

        Method setDeletedFlagMethod = t.getClass().getMethod("setDeletedFlag", Integer.class);
        setDeletedFlagMethod.invoke(t, CommonEnums.DeletedFlagEnum.DELETED_FLAG_1.getCode());

        Method setUpdatorIdMethod = t.getClass().getMethod("setUpdatorId", Integer.class);
        setUpdatorIdMethod.invoke(t, null == loginUserDTO ? 0 : loginUserDTO.getUserId());

        Method setUpdatorNameMethod = t.getClass().getMethod("setUpdatorName", String.class);
        setUpdatorNameMethod.invoke(t, null == loginUserDTO ? "" : loginUserDTO.getUserName());

        Method setUpdateTimeMethod = t.getClass().getMethod("setUpdateTime", Date.class);
        setUpdateTimeMethod.invoke(t, RequestTimeContext.get());
        log.info("DeleteUtil.getDeleteDO,返回值t:{}", JSONObject.toJSONString(t));
        return t;
    }

    public static <T> T getDeleteDOByForeignKey(Class<T> clazz, Integer foreignKey, String foreignKeyName) throws Exception {
        log.info("DeleteUtil.getDeleteDO,输入参数,clazz:{},foreignKey,{},foreignKeyName:{}", clazz, foreignKey, foreignKeyName);
        LoginUserDTO loginUserDTO = null;
        try {
            loginUserDTO = LoginUserContext.get();
        } catch (Exception e) {
            log.error("DeleteUtil.getDeleteDO,异常信息:{}", JSONObject.toJSONString(e));
        }
        String foreignKeyMethod = "set" + foreignKeyName.substring(0, 1).toUpperCase() + foreignKeyName.substring(1);

        T t = clazz.newInstance();
        Method setForeignKeyMethod = t.getClass().getMethod(foreignKeyMethod, Integer.class);
        setForeignKeyMethod.invoke(t, foreignKey);

        Method setDeletedFlagMethod = t.getClass().getMethod("setDeletedFlag", Integer.class);
        setDeletedFlagMethod.invoke(t, CommonEnums.DeletedFlagEnum.DELETED_FLAG_1.getCode());

        Method setUpdatorIdMethod = t.getClass().getMethod("setUpdatorId", Integer.class);
        setUpdatorIdMethod.invoke(t, null == loginUserDTO ? 0 : loginUserDTO.getUserId());

        Method setUpdatorNameMethod = t.getClass().getMethod("setUpdatorName", String.class);
        setUpdatorNameMethod.invoke(t,  null == loginUserDTO ? "" : loginUserDTO.getUserName());

        Method setUpdateTimeMethod = t.getClass().getMethod("setUpdateTime", Date.class);
        setUpdateTimeMethod.invoke(t, RequestTimeContext.get());
        log.info("DeleteUtil.getDeleteDOByForeignKey,返回值t:{}", JSONObject.toJSONString(t));
        return t;
    }

    public static <T> T getDeleteDOByMap(Class<T> clazz, Map<String, Object> map) throws Exception {
        log.info("DeleteUtil.getDeleteDOByMap,输入参数,clazz:{},map:{}", clazz, JSONObject.toJSONString(map));

        LoginUserDTO loginUserDTO = null;
        try {
            loginUserDTO = LoginUserContext.get();
        } catch (Exception e) {
            log.error("DeleteUtil.getDeleteDO,异常信息:{}", JSONObject.toJSONString(e));
        }

        T t = clazz.newInstance();

        for(Map.Entry<String, Object> entry : map.entrySet()){
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            String methodName = "set" + String.valueOf(mapKey.charAt(0)).toUpperCase() + mapKey.substring(1);
            log.info("DeleteUtil.getDeleteDOByMap,中间值,mapKey:{},mapValue:{},methodName:{}", mapKey, JSONObject.toJSONString(mapValue), methodName);
            Method setMethod = t.getClass().getMethod(methodName, Integer.class);
            setMethod.invoke(t, mapValue);
        }

        Method setDeletedFlagMethod = t.getClass().getMethod("setDeletedFlag", Integer.class);
        setDeletedFlagMethod.invoke(t, CommonEnums.DeletedFlagEnum.DELETED_FLAG_1.getCode());

        Method setUpdatorIdMethod = t.getClass().getMethod("setUpdatorId", Integer.class);
        setUpdatorIdMethod.invoke(t, null == loginUserDTO ? 0 : loginUserDTO.getUserId());

        Method setUpdatorNameMethod = t.getClass().getMethod("setUpdatorName", String.class);
        setUpdatorNameMethod.invoke(t, null == loginUserDTO ? "" : loginUserDTO.getUserName());

        Method setUpdateTimeMethod = t.getClass().getMethod("setUpdateTime", Date.class);
        setUpdateTimeMethod.invoke(t, RequestTimeContext.get());

        log.info("DeleteUtil.getDeleteDOByMap,返回值t:{}", JSONObject.toJSONString(t));
        return t;
    }

//    public static void main(String[] args) throws Exception {
//        QualificationInfoDO qualificationInfoDO1 = new QualificationInfoDO();
//        QualificationInfoDO delete = getDeleteDO(QualificationInfoDO.class, 1);
//        System.out.println(JSONObject.toJSON(delete));
//    }

}


