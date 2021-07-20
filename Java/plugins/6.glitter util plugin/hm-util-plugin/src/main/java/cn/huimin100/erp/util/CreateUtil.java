package cn.huimin100.erp.util;

import cn.huimin100.erp.dto.LoginUserDTO;
import cn.huimin100.erp.service.ICallbackBeforeOperateDB;
import cn.huimin100.erp.threadlocal.LoginUserContext;
import cn.huimin100.erp.threadlocal.RequestTimeContext;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 公共更新工具
 *
 * @author limengjun
 * @date 2021/5/13 10:51
 **/
@Log4j2
public class CreateUtil {

    public static <T> T getCreateDO(Class<T> targetClazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        log.info("CreateUtil.getCreateDO,输入参数,targetClazz:{}", targetClazz);
        LoginUserDTO LoginUserDTO = null;
        try {
            LoginUserDTO = LoginUserContext.get();
        } catch (Exception e) {
            log.info("CreateUtil.getCreateDO,异常信息:{}", JSONObject.toJSONString(e));
        }
        log.info("CreateUtil.getCreateDO,中间值,LoginUserDTO:{}", JSONObject.toJSONString(LoginUserDTO));

        T t = targetClazz.newInstance();
        if (null == t) {
            log.info("CreateUtil.getCreateDO,输出参数:null");
            return null;
        }
        Method setUpdatorIdMethod = targetClazz.getMethod("setCreatorId", Integer.class);
        setUpdatorIdMethod.invoke(t, null == LoginUserDTO ? 0 : LoginUserDTO.getUserId());

        Method setUpdatorNameMethod = t.getClass().getMethod("setCreatorName", String.class);
        setUpdatorNameMethod.invoke(t, null == LoginUserDTO ? "" : LoginUserDTO.getUserName());

        Method setUpdateTimeMethod = t.getClass().getMethod("setCreateTime", Date.class);
        setUpdateTimeMethod.invoke(t, RequestTimeContext.get());

        log.info("CreateUtil.getCreateDO,输出参数:{}", JSONObject.toJSONString(t));
        return t;
    }

    public static <T> T vo2DO4Create(Object source, Class<T> targetClazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("CreateUtil.vo2DO4Create,输入参数,source:{},targetClazz:{}", JSONObject.toJSONString(source), targetClazz);
        LoginUserDTO LoginUserDTO = null;
        try {
            LoginUserDTO = LoginUserContext.get();
        } catch (Exception e) {
            log.info("CreateUtil.vo2DO4Create,异常信息:{}", JSONObject.toJSONString(e));
        }
        log.info("CreateUtil.vo2DO4Create,中间值,LoginUserDTO:{}", JSONObject.toJSONString(LoginUserDTO));

        T t = Converter.transferObj(source, targetClazz);
        if (null == t) {
            log.info("CreateUtil.vo2DO4Create,输出参数:null");
            return null;
        }

        Method setUpdatorIdMethod = targetClazz.getMethod("setCreatorId", Integer.class);
        setUpdatorIdMethod.invoke(t, null == LoginUserDTO ? 0 : LoginUserDTO.getUserId());

        Method setUpdatorNameMethod = t.getClass().getMethod("setCreatorName", String.class);
        setUpdatorNameMethod.invoke(t, null == LoginUserDTO ? "" : LoginUserDTO.getUserName());

        Method setUpdateTimeMethod = t.getClass().getMethod("setCreateTime", Date.class);
        setUpdateTimeMethod.invoke(t, RequestTimeContext.get());

        log.info("CreateUtil.vo2DO4Create,输出参数:{}", JSONObject.toJSONString(t));
        return t;
    }

    public static <V,D,M> D insert4SinglelineTable(V vo, Class<D> doClazz, M mapper, ICallbackBeforeOperateDB<D> callbackBeforeOperateDB) throws Exception {
        log.info("CreateUtil.insert4SinglelineTable,输入参数,vo:{},doClazz:{},mapper:{},callbackBeforeOperateDB:{}", JSONObject.toJSONString(vo), doClazz, mapper, callbackBeforeOperateDB);
        D do4insert = CreateUtil.vo2DO4Create(vo, doClazz);

        if (callbackBeforeOperateDB !=null) {
            callbackBeforeOperateDB.beforeInsert(do4insert);
        }
        log.info("CreateUtil.insert4SinglelineTable,中间值,do4insert:{}", JSONObject.toJSONString(do4insert));

        Method insertSelectiveMethod = mapper.getClass().getMethod("insertSelective", doClazz);
        Integer id = (Integer) insertSelectiveMethod.invoke(mapper, do4insert);
        log.info("CreateUtil.insert4SinglelineTable,中间值,id:{},结果值,do4insert:{}", id, JSONObject.toJSONString(do4insert));
        return do4insert;
    }

    public static <V,D,M> List<D> insert4MultilineSubtable(List<V> vos, Class<D> doClazz, M mapper) throws Exception {
        return CreateUtil.insert4MultilineSubtable(vos, doClazz, mapper, null, null, null);
    }

    public static <V,D,M> List<D> insert4MultilineSubtable(List<V> vos, Class<D> doClazz, M mapper, Integer foreignKey, String foreignKeyName) throws Exception {
        return CreateUtil.insert4MultilineSubtable(vos, doClazz, mapper, foreignKey, foreignKeyName, null);
    }

    public static <V,D,M> List<D> insert4MultilineSubtable(List<V> vos, Class<D> doClazz, M mapper, ICallbackBeforeOperateDB<D> callback4Edit) throws Exception {
        return CreateUtil.insert4MultilineSubtable(vos, doClazz, mapper, null, null, callback4Edit);
    }

    public static <V,D,M> List<D> insert4MultilineSubtable(List<V> vos, Class<D> doClazz, M mapper, Integer foreignKey, String foreignKeyName, ICallbackBeforeOperateDB<D> callbackBeforeOperateDB) throws Exception {
        log.info("CreateUtil.insert4MultilineSubtable,输入参数,vos:{},doClazz:{},mapper:{},foreignKey:{},foreignKeyName:{},callbackBeforeOperateDB:{}", JSONObject.toJSONString(vos), doClazz, mapper, foreignKey, foreignKeyName, callbackBeforeOperateDB);
        if (null == vos || vos.size() <=0) {
            return null;
        }
        List<D> dos = new ArrayList<>();

        String foreignKeyMethod = null;
        if (null != foreignKey && StringUtils.isNotBlank(foreignKeyName)) {
            foreignKeyMethod = "set" + foreignKeyName.substring(0, 1).toUpperCase() + foreignKeyName.substring(1);
        }
        for (V vo4create : vos) {
            D do4create = CreateUtil.vo2DO4Create(vo4create, doClazz);
            if (StringUtils.isNotBlank(foreignKeyMethod)) {
                Method setForeignKeyMethod = doClazz.getMethod(foreignKeyMethod, Integer.class);
                setForeignKeyMethod.invoke(do4create, foreignKey);
            }
            if (callbackBeforeOperateDB !=null) {
                callbackBeforeOperateDB.beforeInsert(do4create);
            }
            log.info("CreateUtil.insert4MultilineSubtable,中间值,do4create:{}", JSONObject.toJSONString(do4create));
            Method insertSelectiveMethod = mapper.getClass().getMethod("insertSelective", doClazz);
            insertSelectiveMethod.invoke(mapper, do4create);
            dos.add(do4create);
        }
        return dos;
    }

    public static <D,M> List<D> insert4MultilineSubtable(List<Integer> vos, Class<D> doClazz, M mapper, String integerKeyName, Integer foreignKey, String foreignKeyName, ICallbackBeforeOperateDB<D> callbackBeforeOperateDB) throws Exception {
        log.info("CreateUtil.insert4MultilineSubtable,输入参数,vos:{},doClazz:{},mapper:{},integerKeyName:{},foreignKey:{},foreignKeyName:{},callbackBeforeOperateDB:{}", JSONObject.toJSONString(vos), doClazz, mapper, integerKeyName, foreignKey, foreignKeyName, callbackBeforeOperateDB);
        if (null == vos || vos.size() <=0) {
            return null;
        }
        List<D> dos = new ArrayList<>();

        String integerKeyMethod = null;
        if (StringUtils.isNotBlank(integerKeyName)) {
            integerKeyMethod = "set" + integerKeyName.substring(0, 1).toUpperCase() + integerKeyName.substring(1);
        }
        String foreignKeyMethod = null;
        if (null != foreignKey && StringUtils.isNotBlank(foreignKeyName)) {
            foreignKeyMethod = "set" + foreignKeyName.substring(0, 1).toUpperCase() + foreignKeyName.substring(1);
        }
        for (Integer integerKey : vos) {
            D do4create = CreateUtil.getCreateDO(doClazz);
            if (StringUtils.isNotBlank(integerKeyMethod)) {
                Method setIntegerKeyMethod = doClazz.getMethod(integerKeyMethod, Integer.class);
                setIntegerKeyMethod.invoke(do4create, integerKey);
            }
            if (StringUtils.isNotBlank(foreignKeyMethod)) {
                Method setForeignKeyMethod = doClazz.getMethod(foreignKeyMethod, Integer.class);
                setForeignKeyMethod.invoke(do4create, foreignKey);
            }
            if (callbackBeforeOperateDB !=null) {
                callbackBeforeOperateDB.beforeInsert(do4create);
            }
            log.info("CreateUtil.insert4MultilineSubtable,中间值,do4create:{}", JSONObject.toJSONString(do4create));
            Method insertSelectiveMethod = mapper.getClass().getMethod("insertSelective", doClazz);
            insertSelectiveMethod.invoke(mapper, do4create);
            dos.add(do4create);
        }
        return dos;
    }

//    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        ContractDelayQualificationInfoEditVO contractDelayQualificationInfoEditVO = new ContractDelayQualificationInfoEditVO();
//        contractDelayQualificationInfoEditVO.setId(2);
//        contractDelayQualificationInfoEditVO.setFileName("第二张-修改");
//
//        QualificationInfoDO qualificationInfoDO = vo2DO4Create(contractDelayQualificationInfoEditVO, QualificationInfoDO.class);
//        System.out.println(JSONObject.toJSON(qualificationInfoDO));
//    }

}


