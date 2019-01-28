package com.example.blds.util;


public class Enumeration {
    public enum RET_CODE {
        ;
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;
        public static final int NO_PERMISSION = 2;
        public static final int TOKEN_ERROR = 5;
    }

    public enum SECRET_KEY {
        ;
        /**
         * consultId秘钥
         */
        public static final String CONSULT_ID_KEY = "YN544knjhjUjYN544knjhjUjkj21kj21";
        /**
         * addressId秘钥
         */
        public static final String ADDRESS_ID_KEY = "YN514knjhjUjYN524knjhjUdfg21kj21";
        /**
         * 切片
         */
        public static final String BL_DES_KEY = "kfbucmed88851766kfbucmed88851766";
        /**
         * 评价
         */
        public static final String EVALUATE_ID_KEY = "evaluateucmedljg8880715erqaqaqawar";
        /**
         * 价格
         */
        public static final String PRICE_ID_KEY = "priceucmedljg8880715erqaqaqawardf";
    }

    /**
     * 会诊类型
     **/
    public enum CONSULT_TYPE {
        ;
        /**
         * 病理
         **/
        public static final int BL = 0;
        /**
         * 补充检查
         **/
        public static final int BCJC = 1;
    }

    /**
     * 病例状态码 1草稿；2待支付；3待收货；4待诊断；5已退回
     */
    public enum CONSULT_STATUS {
        ;
        /**
         * 草稿
         */
        public static final int DRAFT = 1;
        /**
         * 待支付
         */
        public static final int UNPAID = 2;
        /**
         * 待收货
         */
        public static final int UNRECEIVED = 3;
        /**
         * 待诊断
         */
        public static final int UNDIAGNOSED = 4;
        /**
         * 已退回
         */
        public static final int RETURNED = 5;

        /**
         * 已诊断
         **/
        public static final int DIAGNOSED = 6;
        /**
         * 待二次诊断
         **/
        public static final int SECONDARYDBC = 12;
        /**
         * 待二次诊断
         **/
        public static final int SECONDARYDIAGNOSIS = 13;

        /**
         * 待审核
         */
        public static final int WAIT_VERIFY = 60;
        /**
         * 审核通过
         */
        public static final int VERIFY_PASS = 61;

        /**
         * 审核不通过
         */
        public static final int VERIFY_NO_PASS = 62;

        /**
         * 已结算
         */
        public static final int FEE_CLEARED = 63;

        /**
         * 待安排
         */
        public static final int TO_BE_ARRANGED = 7;
    }

    /**
     * 病例状态码 1草稿；2待支付；3待收货；4待诊断；5已退回
     */
    public enum VERIFY_STATUS {
        ;

        /**
         * 待审核
         */
        public static final int WAIT_VERIFY = 0;
        /**
         * 审核通过
         */
        public static final int VERIFY_PASS = 1;

        /**
         * 审核不通过
         */
        public static final int VERIFY_NO_PASS = 2;

        /**
         * 已结算
         */
        public static final int FEE_CLEARED = 3;
    }

    /**
     * 切片上传状态
     * process_status
     */
    public enum PROCESS_STATUS {
        ;
        /**
         * 等待上传0
         **/
        public static final int UPLOAD_WAIT = 0;
        /**
         * 正在上传1
         **/
        public static final int UPLOADING = 1;
        /**
         * 上传完成2
         **/
        public static final int UPLOAD_FINISHED = 2;
    }

    /**
     * 评价类型
     **/
    public enum EvaluateType {
        ;
        /**
         * 医生对专家的评价
         **/
        public static final int DOCTOR2EXPERT = 0;
    }

    /**
     * 评价状态
     **/
    public enum EvaluateStatus {
        ;
        /**
         * 0：待审核
         **/
        public static final int UNVERIFY = 0;
        /**
         * 1审核通过
         **/
        public static final int PASS = 1;
        /**
         * 2被撤回
         **/
        public static final int REFUSED = 2;
    }

    /**
     * 删除状态类型
     **/
    public enum Delete {
        ;
        /**
         * 医生对专家的评价
         **/
        public static final int NOT_DELETE = 0;
        public static final int DELETED = 1;
    }

    /**
     * 病例类别枚举，参考 hz_consult 表的 type 字段注释
     */
    public enum ConsultTypeEnum {

        /**
         * 类别为0，病理
         * 类别为1，影像
         * 类别为2，门诊
         * 类别为3，会诊
         */
        BING_LI(0), YING_XIANG(1), MEN_ZHEN(2), HUI_ZHEN(3);

        private int type;

        ConsultTypeEnum(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }
    }

    /**
     * 病例时间字段枚举
     */
    public enum ConsultTimeColumnEnum {

        /**
         * 创建时间，对应 hz_consult 表 create_time 字段
         * 提交时间，对应 hz_consult 表 commit_time 字段
         * 诊断时间，对应 hz_diagnose 表 diagnose_time 字段
         * 审批时间，对应 hz_consult 表 approve_time 字段
         * 更新时间，对应 hz_consult 表 update_time 字段
         */
        CREATE_TIME("create_time"), COMMIT_TIME("commit_time"), DIAGNOSE_TIME("diagnose_time"), APPROVE_TIME
                ("approve_time"), UPDATE_TIME("update_time");

        private String timeColumn;

        ConsultTimeColumnEnum(String timeColumn) {
            this.timeColumn = timeColumn;
        }

        public String getTimeColumn() {
            return this.timeColumn;
        }
    }

    /**
     * 病例类型 id 枚举
     */
    public interface CASE_TYPE_ID {
        /**
         * 常规
         */
        int ROUTINE = 301;
        /**
         * 冰冻
         */
        int FROZEN = 302;
        /**
         * 细胞
         */
        int CELL = 303;
    }

    public interface PRICE_TYPE extends CASE_TYPE_ID{
        /**
         * 补充检查(用户中心价格类型)
         */
        int SUPPLY = 304;

    }


    /**
     * 病例类型 id 枚举
     */
    public enum CaseTypeIdEnum {

        /**
         * 301，常规病理会诊
         * 302，冰冻病理会诊
         * 303，细胞病理会诊
         * 401，紧急会诊
         */
        CHANG_GUI(301), BING_DONG(302), XI_BAO(303), JIN_JI(401);

        private int caseTypeId;

        CaseTypeIdEnum(int caseTypeId) {
            this.caseTypeId = caseTypeId;
        }

        public int getCaseTypeId() {
            return this.caseTypeId;
        }
    }

    /**
     * 超时时间枚举
     */
    public enum TimeoutEnum {

        /**
         * 10，10分钟
         * 60，1小时
         * 1440，24小时
         * 2880，2天
         */
        TEN_MINUTES(10), ONE_HOUR(60), ONE_DAY(1440), TWO_DAYS(2880);

        private int timeout;

        TimeoutEnum(int timeout) {
            this.timeout = timeout;
        }

        public int getTimeout() {
            return this.timeout;
        }
    }

    public enum DoctorTypeEnum {
        ;
        /**
         * 申请医生
         **/
        public static final int DOCTOR = 0;
        /**
         * 专家医生
         **/
        public static final int TOP_DOCTOR = 1;
    }

    public enum SlideTypeEnum {
        ;
        /**
         * 切片类型，0会诊病例切片
         **/
        public static final int Consult = 0;
    }

    public enum BcjcSlideEnum {
        ;
        /**
         * 补充检查切片模式。0数字切片，1邮寄切片',
         **/
        public static final int SZQP = 0;
        public static final int YJQP = 1;
    }

    /**
     * 病例的切片上传模式
     **/
    public enum ConsultSlideTypeEnum {
        ;
        /**
         * 切片模式
         **/
        public static final int SLIDE = 0;
        /**
         * 寄送模式
         **/
        public static final int EXPRESS = 1;
        /**
         * 冰冻预约
         **/
        public static final int FROZEN = 2;
    }

    public enum PdfTemplateEnum {
        ;
        /**
         * 病理模板ID
         **/
        public static final int BL_PDF = 0;
    }

    public enum FileTypeEnum {
        ;
        /**
         * 0电子病历图片
         **/
        public static final int DZBL = 0;
        /**
         * 1快递单
         **/
        public static final int EXPRESS = 1;
        /**
         * 2专家诊断截图
         **/
        public static final int SLIDE_SHOT = 2;
        public static String JPG = "jpg";
        public static String EXCEL = "excel";
        public static String WORD = "word";
        public static String PDF = "pdf";
        public static String PPT = "ppt";
        public static String TXT = "txt";
        public static String RAR = "rar";
    }
}
