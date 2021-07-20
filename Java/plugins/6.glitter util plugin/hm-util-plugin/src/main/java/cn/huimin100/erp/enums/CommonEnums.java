package cn.huimin100.erp.enums;

/**
 * 公共枚举
 *
 * @author wangshuai
 * @date 2020/5/11 14:51
 **/
public class CommonEnums {
    public CommonEnums() {
    }

    /**
     * 变更状态
     */
    public static enum ChEnum{
        /**
         *
         */
        DELETE_TYPE(1, "删除"),
        UPDATE_TYPE(2,"修改"),
        ADD_TYPE(3,"添加");
        private int code;
        private String name;

        private ChEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static ChEnum resolve(int code) {
            switch (code) {
                case 1:
                    return DELETE_TYPE;
                case 2:
                    return UPDATE_TYPE;
                case 3:
                    return ADD_TYPE;
                default:
                    return null;
            }
        }
    }

    /**
     * 启用状态
     */
    public static enum StatusEnum {
        /**
         * 状态 0:停用; 1:启用;
         */
        STATUS_0(0, "停用"),
        STATUS_1(1, "启用");
        private int code;
        private String name;

        private StatusEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static StatusEnum resolve(int code) {
            switch (code) {
                case 0:
                    return STATUS_0;
                case 1:
                    return STATUS_1;
                default:
                    return null;
            }
        }
    }

    /**
     * 启用状态
     */
    public static enum StatusEnum2 {
        /**
         * 状态 1:生效; 2:终止;
         */
        STATUS_1(1, "生效"),
        STATUS_2(2, "终止");
        private int code;
        private String name;

        private StatusEnum2(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static StatusEnum2 resolve(int code) {
            switch (code) {
                case 1:
                    return STATUS_1;
                case 2:
                    return STATUS_2;
                default:
                    return null;
            }
        }
    }

    public static int getDetetedN(){
        return DeletedFlagEnum.DELETED_FLAG_0.getCode();
    }
    public static int getDetetedY(){
        return DeletedFlagEnum.DELETED_FLAG_1.getCode();
    }

    /**
     * 删除标识
     */
    public static enum DeletedFlagEnum {
        /**
         * 删除标识 0:未删除 1:删除
         */
        DELETED_FLAG_0(0, "未删除"),
        DELETED_FLAG_1(1, "删除");
        private int code;
        private String name;

        private DeletedFlagEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static DeletedFlagEnum resolve(int code) {
            switch (code) {
                case 0:
                    return DELETED_FLAG_0;
                case 1:
                    return DELETED_FLAG_1;
                default:
                    return null;
            }
        }
    }

    /**
     * 归档状态
     */
    public static enum ArchiveStatusEnum {
        /**
         *   归档状态 0:未归档 1:已归档
         */
        ARCHIVE_STATUS_0(0, "未归档"),
        ARCHIVE_STATUS_1(1, "已归档");
        private int code;
        private String name;

        private ArchiveStatusEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static ArchiveStatusEnum resolve(int code) {
            switch (code) {
                case 0:
                    return ARCHIVE_STATUS_0;
                case 1:
                    return ARCHIVE_STATUS_1;
                default:
                    return null;
            }
        }
    }

    /**
     * 数据编辑类型
     */
    public static enum EditTypeEnum {
        /**
         * 数据提交类型 1:暂存编辑 2:驳回编辑
         */
        EDIT_TYPE_1(1, "暂存编辑"),
        EDIT_TYPE_2(2, "驳回编辑");

        private int code;
        private String name;

        private EditTypeEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static EditTypeEnum resolve(int code) {
            switch (code) {
                case -1:
                    return EDIT_TYPE_1;
                case 1:
                    return EDIT_TYPE_2;
                default:
                    return null;
            }
        }
    }

    /**
     * 数据提交类型
     */
    public static enum SubmitTypeEnum {
        /**
         * 数据提交类型 -1:暂存 1:提交
         */
        SUBMIT_TYPE_NEGATIVE_1(-1, "暂存"),
        SUBMIT_TYPE_1(1, "提交");

        private int code;
        private String name;

        private SubmitTypeEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static SubmitTypeEnum resolve(int code) {
            switch (code) {
                case -1:
                    return SUBMIT_TYPE_NEGATIVE_1;
                case 1:
                    return SUBMIT_TYPE_1;
                default:
                    return null;
            }
        }
    }

    /**
     * 审核状态
     */
    public static enum AuditStatusEnum {
        /**
         * 审核状态 0 -; 1:审核中;2:审核通过;3:审核驳回
         */
        AUDIT_STATUS_0(0, "-"),
        AUDIT_STATUS_1(1, "审核中"),
        AUDIT_STATUS_2(2, "审核通过"),
        AUDIT_STATUS_3(3, "审核驳回");
        private int code;
        private String name;

        private AuditStatusEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static AuditStatusEnum resolve(int code) {
            switch (code) {
                case 1:
                    return AUDIT_STATUS_1;
                case 2:
                    return AUDIT_STATUS_2;
                case 3:
                    return AUDIT_STATUS_3;
                default:
                    return null;
            }
        }
    }

    /**
     * 变更-数据来源
     */
    public static enum DataSourceEnum {
        /**
         * 数据来源 1:新增; 2:变更
         */
        DATA_SOURCE_1(1, "新增"),
        DATA_SOURCE_2(2, "变更");

        private int code;
        private String name;

        private DataSourceEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static DataSourceEnum resolve(int code) {
            switch (code) {
                case 1:
                    return DATA_SOURCE_1;
                case 2:
                    return DATA_SOURCE_2;
                default:
                    return null;
            }
        }
    }

    /**
     * 操作类型
     */
    public static enum OperateTypeEnum {
        /**
         * 操作类型 1:系统生成; 2:人工操作
         */
        OPERATE_TYPE_1(1, "系统生成"),
        OPERATE_TYPE_2(2, "人工操作");

        private int code;
        private String name;

        private OperateTypeEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static OperateTypeEnum resolve(int code) {
            switch (code) {
                case 1:
                    return OPERATE_TYPE_1;
                case 2:
                    return OPERATE_TYPE_2;
                default:
                    return null;
            }
        }
    }

    /**
     * 冻结申请类型 1:冻结;2:解冻
     */
    public static enum FreezeApplyType {
        /**
         * 冻结申请类型 1:冻结;2:解冻
         */
        FREEZE_APPLY_TYPE_1(1, "冻结"),
        FREEZE_APPLY_TYPE_2(2, "解冻");

        private int code;
        private String name;

        private FreezeApplyType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static FreezeApplyType resolve(int code) {
            switch (code) {
                case 1:
                    return FREEZE_APPLY_TYPE_1;
                case 2:
                    return FREEZE_APPLY_TYPE_2;
                default:
                    return null;
            }
        }
    }

}
