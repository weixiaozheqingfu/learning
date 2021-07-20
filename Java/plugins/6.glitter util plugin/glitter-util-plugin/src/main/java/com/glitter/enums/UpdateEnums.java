package com.glitter.enums;

/**
 * 合同相关枚举
 *
 * @author limengjun
 * @date 2021/5/19 14:04
 **/
public class UpdateEnums {
    public UpdateEnums() {
    }

    /**
     * 子表多条记录时,涉及到的变更类型
     */
    public static enum ModifyModeEnum {
        /**
         * 变更基础类型 1:删除;2:更新;4:新增
         */
        UPDATE_MODE_1(1, "删除"),
        UPDATE_MODE_2(2, "更新"),
        UPDATE_MODE_3(3, "删除,更新"),
        UPDATE_MODE_4(4, "新增"),
        UPDATE_MODE_5(5, "删除,新增"),
        UPDATE_MODE_6(6, "更新,新增"),
        UPDATE_MODE_7(7, "删除,更新,新增");

        private int code;
        private String name;

        private ModifyModeEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static ModifyModeEnum resolve(int code) {
            switch (code) {
                case 1:
                    return UPDATE_MODE_1;
                case 2:
                    return UPDATE_MODE_2;
                case 3:
                    return UPDATE_MODE_3;
                case 4:
                    return UPDATE_MODE_4;
                case 5:
                    return UPDATE_MODE_5;
                case 6:
                    return UPDATE_MODE_6;
                case 7:
                    return UPDATE_MODE_7;
                default:
                    return null;
            }
        }
    }

}
