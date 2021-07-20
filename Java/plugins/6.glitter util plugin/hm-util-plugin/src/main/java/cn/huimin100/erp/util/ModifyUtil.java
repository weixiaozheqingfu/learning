package cn.huimin100.erp.util;

import cn.huimin100.erp.dto.*;
import cn.huimin100.erp.threadlocal.LoginUserContext;
import cn.huimin100.erp.threadlocal.RequestTimeContext;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 公共更新工具
 *
 * @author limengjun
 * @date 2021/5/13 10:51
 **/
@Log4j2
public class ModifyUtil {
    public static <D> D getDO4Update(D d) throws Exception {
        log.info("ModifyUtil.getDO4Update,输入参数,d:{}", JSONObject.toJSONString(d));
        LoginUserDTO loginUserDTO = null;
        try {
            loginUserDTO = LoginUserContext.get();
        } catch (Exception e) {
            log.error("ModifyUtil.getDO4Update,异常信息:{}", JSONObject.toJSONString(e));
        }

        Method setUpdatorIdMethod = d.getClass().getMethod("setUpdatorId", Integer.class);
        setUpdatorIdMethod.invoke(d, null == loginUserDTO ? 0 : loginUserDTO.getUserId());

        Method setUpdatorNameMethod = d.getClass().getMethod("setUpdatorName", String.class);
        setUpdatorNameMethod.invoke(d, null == loginUserDTO ? "" : loginUserDTO.getUserName());

        Method setUpdateTimeMethod = d.getClass().getMethod("setUpdateTime", Date.class);
        setUpdateTimeMethod.invoke(d, RequestTimeContext.get());
        log.info("ModifyUtil.getDO4Update,返回值,d:{}", JSONObject.toJSONString(d));
        return d;
    }

    public static <D> D vo2DO4Update(Object vo, Class<D> doClazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("ModifyUtil.vo2DO4Update,输入参数,vo:{},doClazz:{}", JSONObject.toJSONString(vo), doClazz);
        LoginUserDTO loginUserDTO = null;
        try {
            loginUserDTO = LoginUserContext.get();
        } catch (Exception e) {
            log.info("ModifyUtil.vo2DO4Update,异常信息:{}", JSONObject.toJSONString(e));
        }
        log.info("ModifyUtil.vo2DO4Update,中间值,loginUserInfoDTO:{}", JSONObject.toJSONString(loginUserDTO));

        D d = Converter.transferObj(vo, doClazz);
        if (null == d) {
            log.info("ModifyUtil.vo2DO4Update,输出参数:null");
            return null;
        }

        Method setUpdatorIdMethod = doClazz.getMethod("setUpdatorId", Integer.class);
        setUpdatorIdMethod.invoke(d, null == loginUserDTO ? 0 : loginUserDTO.getUserId());

        Method setUpdatorNameMethod = doClazz.getMethod("setUpdatorName", String.class);
        setUpdatorNameMethod.invoke(d, null == loginUserDTO ? "" : loginUserDTO.getUserName());

        Method setUpdateTimeMethod = doClazz.getMethod("setUpdateTime", Date.class);
        setUpdateTimeMethod.invoke(d, RequestTimeContext.get());

        log.info("ModifyUtil.vo2DO4Update,输出参数:{}", JSONObject.toJSONString(d));
        return d;
    }

    public static <V,D> D vo2DO4Update(V v, D d) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        log.info("ModifyUtil.vo2DO4Update,输入参数,v:{},d:{}", JSONObject.toJSONString(v), JSONObject.toJSONString(d));
        if (null == v || null == d) {
            throw new RuntimeException("参数异常转换失败");
        }
        LoginUserDTO loginUserDTO = null;
        try {
            loginUserDTO = LoginUserContext.get();
        } catch (Exception e) {
            log.info("ModifyUtil.vo2DO4Update,异常信息:{}", JSONObject.toJSONString(e));
        }
        log.info("ModifyUtil.vo2DO4Update,中间值,loginUserInfoDTO:{}", JSONObject.toJSONString(loginUserDTO));

        ModelUtils.copyProperties(v, d);

        Method setUpdatorIdMethod = d.getClass().getMethod("setUpdatorId", Integer.class);
        setUpdatorIdMethod.invoke(d, null == loginUserDTO ? 0 : loginUserDTO.getUserId());

        Method setUpdatorNameMethod = d.getClass().getMethod("setUpdatorName", String.class);
        setUpdatorNameMethod.invoke(d, null == loginUserDTO ? "" : loginUserDTO.getUserName());

        Method setUpdateTimeMethod = d.getClass().getMethod("setUpdateTime", Date.class);
        setUpdateTimeMethod.invoke(d, RequestTimeContext.get());

        log.info("ModifyUtil.vo2DO4Update,输出参数:{}", JSONObject.toJSONString(d));
        return d;
    }

    public static <D, V> CommonModifyDTO<V> getCommonModifyDTO(List<D> beforeDataList, List<V> afterDataList, Class<D> clazzD, Class<V> clazzV) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("ModifyUtil.getCommonModifyDTO,输入参数,beforeDataList:{},afterDataList:{}", JSONObject.toJSONString(beforeDataList), JSONObject.toJSONString(afterDataList));
        CommonModifyDTO result = new CommonModifyDTO();
        // 1.处理dos为空的情况
        if (null == beforeDataList || beforeDataList.size() <= 0) {
            if (null == afterDataList || afterDataList.size() <= 0) {
                log.info("ModifyUtil.getCommonModifyDTO,result1:{}", JSONObject.toJSONString(result));
                return result;
            } else {
                result.setInsertRecords(afterDataList);
                log.info("ModifyUtil.getCommonModifyDTO,result2:{}", JSONObject.toJSONString(result));
                return result;
            }
        }
        // 2.处理vos为空的情况
        if (null == afterDataList || afterDataList.size() <= 0) {
            List<Integer> deleteIds = new ArrayList<>();
            for (D d : beforeDataList) {
                Method method = clazzD.getMethod("getId");
                Integer id = (Integer) method.invoke(d);
                deleteIds.add(id);
            }
            result.setDeleteIds(deleteIds);
            log.info("ModifyUtil.getCommonModifyDTO,result3:{}", JSONObject.toJSONString(result));
            return result;
        }
        // 3.处理dos与vos都不为空的情况
        Set<Integer> doIds = new HashSet<>();
        Set<Integer> voIds = new HashSet<>();
        Map<Integer, D> doMap = new HashMap<>();
        Map<Integer, V> voMap = new HashMap<>();

        List<Integer> deleteIds = new ArrayList<>();
        List<V> insertRecords = new ArrayList<>();
        List<V> updateRecords = new ArrayList<>();
        for (D d : beforeDataList) {
            Method method = clazzD.getMethod("getId");
            Integer id = (Integer) method.invoke(d);
            doIds.add(id);
            doMap.put(id, d);
        }
        for (V v : afterDataList) {
            Method method = clazzV.getMethod("getId");
            Integer id = (Integer) method.invoke(v);
            if (null != id) {
                voIds.add(id);
                voMap.put(id, v);
            } else {
                // 新增
                insertRecords.add(v);
            }
        }
        for (Integer d : doIds) {
            if (voIds.contains(d)) {
                updateRecords.add(voMap.get(d));
            } else {
                deleteIds.add(d);
            }
        }
        result.setDeleteIds(deleteIds);
        result.setInsertRecords(insertRecords);
        result.setUpdateRecords(updateRecords);
        log.info("ModifyUtil.getCommonModifyDTO,result4:{}", JSONObject.toJSONString(result));
        return result;
    }

    public static <D, V> ModifyDTO<D, V> getModifyDTO(List<D> beforeDataList, List<V> afterDataList, Class<D> clazzD, Class<V> clazzV) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("ModifyUtil.getModifyDTO,输入参数,beforeDataList:{},afterDataList:{}", JSONObject.toJSONString(beforeDataList), JSONObject.toJSONString(afterDataList));
        ModifyDTO result = new ModifyDTO();
        // 1.处理dos为空的情况
        if (null == beforeDataList || beforeDataList.size() <= 0) {
            if (null == afterDataList || afterDataList.size() <= 0) {
                log.info("ModifyUtil.getModifyDTO,result1:{}", JSONObject.toJSONString(result));
                return result;
            } else {
                result.setInsertRecords(afterDataList);
                log.info("ModifyUtil.getModifyDTO,result2:{}", JSONObject.toJSONString(result));
                return result;
            }
        }
        // 2.处理vos为空的情况
        if (null == afterDataList || afterDataList.size() <= 0) {
            List<Integer> deleteIds = new ArrayList<>();
            for (D d : beforeDataList) {
                Method method = clazzD.getMethod("getId");
                Integer id = (Integer) method.invoke(d);
                deleteIds.add(id);
            }
            result.setDeleteIds(deleteIds);
            log.info("ModifyUtil.getModifyDTO,result3:{}", JSONObject.toJSONString(result));
            return result;
        }
        // 3.处理dos与vos都不为空的情况
        Set<Integer> doIds = new HashSet<>();
        Set<Integer> voIds = new HashSet<>();
        Map<Integer, D> doMap = new HashMap<>();
        Map<Integer, V> voMap = new HashMap<>();

        List<Integer> deleteIds = new ArrayList<>();
        List<V> insertRecords = new ArrayList<>();
        List<D> updateRecords = new ArrayList<>();
        for (D d : beforeDataList) {
            Method method = clazzD.getMethod("getId");
            Integer id = (Integer) method.invoke(d);
            doIds.add(id);
            doMap.put(id, d);
        }
        for (V v : afterDataList) {
            Method method = clazzV.getMethod("getId");
            Integer id = (Integer) method.invoke(v);
            if (null != id) {
                voIds.add(id);
                voMap.put(id, v);
            } else {
                // 新增
                insertRecords.add(v);
            }
        }
        for (Integer d : doIds) {
            if (voIds.contains(d)) {
                D doObject = doMap.get(d);
                V voObject = voMap.get(d);
                ModelUtils.copyProperties(voObject, doObject);
                updateRecords.add(doObject);
            } else {
                deleteIds.add(d);
            }
        }
        result.setDeleteIds(deleteIds);
        result.setInsertRecords(insertRecords);
        result.setUpdateRecords(updateRecords);
        log.info("ModifyUtil.getModifyDTO,result4:{}", JSONObject.toJSONString(result));
        return result;
    }

    public static <D, V> CommonModifyDTO<V> getCommonModifyDTO(List<D> beforeDataList, List<V> afterDataList, Class<D> clazzD, Class<V> clazzV, String ... keyNames) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("ModifyUtil.getCommonModifyDTO,输入参数,beforeDataList:{},afterDataList:{},comparekeys:{}", JSONObject.toJSONString(beforeDataList), JSONObject.toJSONString(afterDataList), JSONObject.toJSONString(keyNames));
        CommonModifyDTO result = new CommonModifyDTO();
        // 1.处理dos为空的情况
        if (null == beforeDataList || beforeDataList.size() <= 0) {
            if (null == afterDataList || afterDataList.size() <= 0) {
                log.info("ModifyUtil.getCommonModifyDTO,result1:{}", JSONObject.toJSONString(result));
                return result;
            } else {
                result.setInsertRecords(afterDataList);
                log.info("ModifyUtil.getCommonModifyDTO,result2:{}", JSONObject.toJSONString(result));
                return result;
            }
        }
        // 2.处理vos为空的情况
        if (null == afterDataList || afterDataList.size() <= 0) {
            List<Integer> deleteIds = new ArrayList<>();
            for (D d : beforeDataList) {
                Method method = clazzD.getMethod("getId");
                Integer id = (Integer) method.invoke(d);
                deleteIds.add(id);
            }
            result.setDeleteIds(deleteIds);
            log.info("ModifyUtil.getCommonModifyDTO,result3:{}", JSONObject.toJSONString(result));
            return result;
        }
        // 3.处理dos与vos都不为空的情况
        Set<String> doUnionkeys = new HashSet<>();
        Set<String> voUnionkeys = new HashSet<>();
        Map<String, D> doMap = new HashMap<>();
        Map<String, V> voMap = new HashMap<>();

        List<Integer> deleteIds = new ArrayList<>();
        List<V> insertRecords = new ArrayList<>();
        List<V> updateRecords = new ArrayList<>();
        for (D d : beforeDataList) {
            String unionkey = ModifyUtil.getKeysValue(clazzD, d, keyNames);
            log.info("ModifyUtil.getCommonModifyDTO,中间值,beforeDataList,unionkey:{}", unionkey);
            doUnionkeys.add(unionkey);
            doMap.put(unionkey, d);
        }
        for (V v : afterDataList) {
            String unionkey = ModifyUtil.getKeysValue(clazzD, v, keyNames);
            log.info("ModifyUtil.getCommonModifyDTO,中间值,afterDataList,unionkey:{}", unionkey);
            voUnionkeys.add(unionkey);
            voMap.put(unionkey, v);
        }
        log.info("ModifyUtil.getCommonModifyDTO,中间值,doUnionkeys:{},voUnionkeys:{},doMap:{},voMap:{}", JSONObject.toJSONString(doUnionkeys), JSONObject.toJSONString(voUnionkeys), JSONObject.toJSONString(doMap), JSONObject.toJSONString(voMap));
        for (String doUnionkey : doUnionkeys) {
            if (voUnionkeys.contains(doUnionkey)) {
                V v = voMap.get(doUnionkey);
                D d = doMap.get(doUnionkey);
                Method getIdMethod = clazzD.getMethod("getId");
                Integer id = (Integer) getIdMethod.invoke(d);
                Method setIdMethod = clazzV.getMethod("setId", Integer.class);
                setIdMethod.invoke(v, id);
                updateRecords.add(v);
            } else {
                D d = doMap.get(doUnionkey);
                Method getIdMethod = clazzD.getMethod("getId");
                Integer id = (Integer) getIdMethod.invoke(d);
                deleteIds.add(id);
            }
        }
        for (String voUnionkey : voUnionkeys) {
            if (!doUnionkeys.contains(voUnionkey)) {
                V v = voMap.get(voUnionkey);
                insertRecords.add(v);
            }
        }
        result.setDeleteIds(deleteIds);
        result.setInsertRecords(insertRecords);
        result.setUpdateRecords(updateRecords);
        log.info("ModifyUtil.getCommonModifyDTO,result4:{}", JSONObject.toJSONString(result));
        return result;
    }

    public static <D, V> ModifyDTO<D, V> getModifyDTO(List<D> beforeDataList, List<V> afterDataList, Class<D> clazzD, Class<V> clazzV, String ... keyNames) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("ModifyUtil.getModifyDTO,输入参数,beforeDataList:{},afterDataList:{},comparekeys:{}", JSONObject.toJSONString(beforeDataList), JSONObject.toJSONString(afterDataList), JSONObject.toJSONString(keyNames));
        ModifyDTO result = new ModifyDTO();
        // 1.处理dos为空的情况
        if (null == beforeDataList || beforeDataList.size() <= 0) {
            if (null == afterDataList || afterDataList.size() <= 0) {
                log.info("ModifyUtil.getModifyDTO,result1:{}", JSONObject.toJSONString(result));
                return result;
            } else {
                result.setInsertRecords(afterDataList);
                log.info("ModifyUtil.getModifyDTO,result2:{}", JSONObject.toJSONString(result));
                return result;
            }
        }
        // 2.处理vos为空的情况
        if (null == afterDataList || afterDataList.size() <= 0) {
            List<Integer> deleteIds = new ArrayList<>();
            for (D d : beforeDataList) {
                Method method = clazzD.getMethod("getId");
                Integer id = (Integer) method.invoke(d);
                deleteIds.add(id);
            }
            result.setDeleteIds(deleteIds);
            log.info("ModifyUtil.getModifyDTO,result3:{}", JSONObject.toJSONString(result));
            return result;
        }
        // 3.处理dos与vos都不为空的情况
        Set<String> doUnionkeys = new HashSet<>();
        Set<String> voUnionkeys = new HashSet<>();
        Map<String, D> doMap = new HashMap<>();
        Map<String, V> voMap = new HashMap<>();

        List<Integer> deleteIds = new ArrayList<>();
        List<V> insertRecords = new ArrayList<>();
        List<D> updateRecords = new ArrayList<>();
        for (D d : beforeDataList) {
            String unionkey = ModifyUtil.getKeysValue(clazzD, d, keyNames);
            log.info("ModifyUtil.getModifyDTO,中间值,beforeDataList,unionkey:{}", unionkey);
            doUnionkeys.add(unionkey);
            doMap.put(unionkey, d);
        }
        for (V v : afterDataList) {
            String unionkey = ModifyUtil.getKeysValue(clazzD, v, keyNames);
            log.info("ModifyUtil.getModifyDTO,中间值,afterDataList,unionkey:{}", unionkey);
            voUnionkeys.add(unionkey);
            voMap.put(unionkey, v);
        }
        log.info("ModifyUtil.getModifyDTO,中间值,doUnionkeys:{},voUnionkeys:{},doMap:{},voMap:{}", JSONObject.toJSONString(doUnionkeys), JSONObject.toJSONString(voUnionkeys), JSONObject.toJSONString(doMap), JSONObject.toJSONString(voMap));
        for (String doUnionkey : doUnionkeys) {
            if (voUnionkeys.contains(doUnionkey)) {
                V v = voMap.get(doUnionkey);
                D d = doMap.get(doUnionkey);
                ModelUtils.copyProperties(v, d);
                updateRecords.add(d);
            } else {
                D d = doMap.get(doUnionkey);
                Method getIdMethod = clazzD.getMethod("getId");
                Integer id = (Integer) getIdMethod.invoke(d);
                deleteIds.add(id);
            }
        }
        for (String voUnionkey : voUnionkeys) {
            if (!doUnionkeys.contains(voUnionkey)) {
                V v = voMap.get(voUnionkey);
                insertRecords.add(v);
            }
        }
        result.setDeleteIds(deleteIds);
        result.setInsertRecords(insertRecords);
        result.setUpdateRecords(updateRecords);
        log.info("ModifyUtil.getModifyDTO,result4:{}", JSONObject.toJSONString(result));
        return result;
    }

    public static CommonModify4Simple4IntegerDTO getCommonModify4Simple4IntegerDTO(List<Integer> dbIds, List<Integer> modifyIds){
        CommonModify4Simple4IntegerDTO result = new CommonModify4Simple4IntegerDTO();
        if ((dbIds == null || dbIds.size() <= 0) && (modifyIds == null || modifyIds.size() <= 0)) {
            return result;
        }

        List<Integer> deleteIds = null;
        List<Integer> insertIds = null;
        if (dbIds == null || dbIds.size() <= 0) {
            result.setInsertIds(modifyIds);
            return result;
        }

        if (modifyIds == null || modifyIds.size() <= 0) {
            result.setDeleteIds(dbIds);
            return result;
        }

        insertIds = new ArrayList<>();
        deleteIds = new ArrayList<>();

        for (Integer dbId : dbIds) {
            if (!modifyIds.contains(dbId)) {
                deleteIds.add(dbId);
            }
        }
        for (Integer modifyId : modifyIds) {
            if (!dbIds.contains(modifyId)) {
                insertIds.add(modifyId);
            }
        }

        result.setInsertIds(insertIds);
        result.setDeleteIds(deleteIds);
        return result;
    }

    public static CommonModify4Simple4StringDTO getCommonModify4Simple4StringDTO(List<String> dbIds, List<String> modifyIds){
        CommonModify4Simple4StringDTO result = new CommonModify4Simple4StringDTO();
        if ((dbIds == null || dbIds.size() <= 0) && (modifyIds == null || modifyIds.size() <= 0)) {
            return result;
        }

        List<String> deleteIds = null;
        List<String> insertIds = null;
        if (dbIds == null || dbIds.size() <= 0) {
            result.setInsertIds(modifyIds);
            return result;
        }

        if (modifyIds == null || modifyIds.size() <= 0) {
            result.setDeleteIds(dbIds);
            return result;
        }

        insertIds = new ArrayList<>();
        deleteIds = new ArrayList<>();

        for (String dbId : dbIds) {
            if (!modifyIds.contains(dbId)) {
                deleteIds.add(dbId);
            }
        }
        for (String modifyId : modifyIds) {
            if (!dbIds.contains(modifyId)) {
                insertIds.add(modifyId);
            }
        }
        result.setInsertIds(insertIds);
        result.setDeleteIds(deleteIds);
        return result;
    }

    private static String getKeysValue(Class clazzD, Object d, String[] keyNames) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String keysValue = "";
        for (String keyName : keyNames) {
            String methodName = ModifyUtil.getMethodName4Get(keyName);
            Method method = clazzD.getMethod(methodName);
            Object o = method.invoke(d);
            if (null == o) {
                throw new RuntimeException("数据异常");
            }
            String value = String.valueOf(o);
            keysValue += value + "_";
        }
        return keysValue;
    }

    private static String getMethodName4Get(String keyName) {
        String methodName = null;
        if (StringUtils.isNotBlank(keyName)) {
            methodName = "get" + keyName.substring(0, 1).toUpperCase() + keyName.substring(1);
        }
        return methodName;
    }

//    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        List<QualificationInfoDO> qualificationInfoDOS = new ArrayList<>();
//        List<ContractDelayQualificationInfoEditVO> contractDelayQualificationInfoEditVOS = new ArrayList<>();
//
//        QualificationInfoDO qualificationInfoDO1 = new QualificationInfoDO();
//        QualificationInfoDO qualificationInfoDO2 = new QualificationInfoDO();
//        qualificationInfoDO1.setId(1);
//        qualificationInfoDO1.setFileName("第一张");
//        qualificationInfoDO2.setId(2);
//        qualificationInfoDO2.setFileName("第二张");
//        qualificationInfoDOS.add(qualificationInfoDO1);
//        qualificationInfoDOS.add(qualificationInfoDO2);
//
//        ContractDelayQualificationInfoEditVO contractDelayQualificationInfoEditVO1 = new ContractDelayQualificationInfoEditVO();
//        ContractDelayQualificationInfoEditVO contractDelayQualificationInfoEditVO2 = new ContractDelayQualificationInfoEditVO();
//        contractDelayQualificationInfoEditVO1.setId(2);
//        contractDelayQualificationInfoEditVO1.setFileName("第二张-修改");
//        contractDelayQualificationInfoEditVO2.setFileName("第二张-修改");
//        contractDelayQualificationInfoEditVO2.setFileName("第三张");
//        contractDelayQualificationInfoEditVOS.add(contractDelayQualificationInfoEditVO1);
//        contractDelayQualificationInfoEditVOS.add(contractDelayQualificationInfoEditVO2);
//
//        CommonModifyDTO commonModifyDTO = getCommonModifyDTO(qualificationInfoDOS, contractDelayQualificationInfoEditVOS, QualificationInfoDO.class, ContractDelayQualificationInfoEditVO.class);
//        System.out.println(JSONObject.toJSON(commonModifyDTO));
//
//
//        List<ShopAccountRoleInfoDO> shopAccountRoleInfoVOS = new ArrayList<>();
//        List<ShopAccountRoleInfoDO> shopAccountRoleInfoDOS = new ArrayList<>();
//
//        ShopAccountRoleInfoDO shopAccountRoleInfoVO2 = new ShopAccountRoleInfoDO();
//        ShopAccountRoleInfoDO shopAccountRoleInfoVO3 = new ShopAccountRoleInfoDO();
//        ShopAccountRoleInfoDO shopAccountRoleInfoVO4 = new ShopAccountRoleInfoDO();
//        shopAccountRoleInfoVOS.add(shopAccountRoleInfoVO2);
//        shopAccountRoleInfoVOS.add(shopAccountRoleInfoVO3);
//        shopAccountRoleInfoVOS.add(shopAccountRoleInfoVO4);
//
//        ShopAccountRoleInfoDO shopAccountRoleInfoDO1 = new ShopAccountRoleInfoDO();
//        ShopAccountRoleInfoDO shopAccountRoleInfoDO2 = new ShopAccountRoleInfoDO();
//        ShopAccountRoleInfoDO shopAccountRoleInfoDO3 = new ShopAccountRoleInfoDO();
//        shopAccountRoleInfoDOS.add(shopAccountRoleInfoDO1);
//        shopAccountRoleInfoDOS.add(shopAccountRoleInfoDO2);
//        shopAccountRoleInfoDOS.add(shopAccountRoleInfoDO3);
//
//        shopAccountRoleInfoVO2.setShopInfoId(1);
//        shopAccountRoleInfoVO2.setUserPhone("131");
//        shopAccountRoleInfoVO2.setRoleId(1);
//        shopAccountRoleInfoVO3.setShopInfoId(1);
//        shopAccountRoleInfoVO3.setUserPhone("131");
//        shopAccountRoleInfoVO3.setRoleId(2);
//        shopAccountRoleInfoVO4.setShopInfoId(1);
//        shopAccountRoleInfoVO4.setUserPhone("132");
//        shopAccountRoleInfoVO4.setRoleId(1);
//
//        shopAccountRoleInfoDO1.setId(1);
//        shopAccountRoleInfoDO1.setShopInfoId(1);
//        shopAccountRoleInfoDO1.setUserPhone("139");
//        shopAccountRoleInfoDO1.setRoleId(1);
//        shopAccountRoleInfoDO2.setId(2);
//        shopAccountRoleInfoDO2.setShopInfoId(1);
//        shopAccountRoleInfoDO2.setUserPhone("131");
//        shopAccountRoleInfoDO2.setRoleId(1);
//        shopAccountRoleInfoDO3.setId(3);
//        shopAccountRoleInfoDO3.setShopInfoId(1);
//        shopAccountRoleInfoDO3.setUserPhone("131");
//        shopAccountRoleInfoDO3.setRoleId(2);
//
//        CommonModifyDTO commonModifyDTO2 = getCommonModifyDTO(shopAccountRoleInfoDOS, shopAccountRoleInfoVOS, ShopAccountRoleInfoDO.class, ShopAccountRoleInfoDO.class, "shopInfoId", "userPhone", "roleId");
//        System.out.println(JSONObject.toJSON(commonModifyDTO2));
//
//    }

}


