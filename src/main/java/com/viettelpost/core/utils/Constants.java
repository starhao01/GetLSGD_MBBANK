package com.viettelpost.core.utils;

/**
 * Created by Oto on 16/10/2018.
 */
public class Constants {
    public final static String PREFIX = "Bearer ";

    public final static String ssoUrl = "https://sso2.viettel.vn:8001/sso/v1/tickets";

    public enum TpKafka {
        DONG_BO_TON("DONG_BO_TON_001", "Tồn ca, xóa nhóm gom bill"),
        PHAN_CONG("PHAN_CONG_001", "Phân công mới"),
        CHUYEN_PHAN_CONG("CHUYEN_PHAN_CONG_001", "Chuyển phân công"),
        CHUYEN_PHAN_CONG_NHOM("CHUYEN_PHAN_CONG_002", "Chuyển phân công theo group bill"),
        HOAN_THANH_PHANCONG("HOAN_THANH_PHANCONG_001", "Hoàn thành phân công"),
        PHIEU_GUI_NHAN("PHIEU_GUI_NHAN_001", "Lưu thông tin phiếu gửi nhận"),
        GACH_BAO_PHAT("GACH_BAO_PHAT_001", "Gạch báo phát");
        private String code;
        private String name;

        TpKafka(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public enum JOURNEY_KEY {
        THOI_GIAN_16("THOI_GIAN_16", "Time as Long yyyyMMddHHmmss"),
        MA_BUUCUC("MA_BUUCUC", "ma buuc cuc"),
        TRANG_THAI("TRANG_THAI", "status");
        private String code;
        private String desc;

        JOURNEY_KEY(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return this.code;
        }
    }

    public enum STATUS {
        E200(200, "Success"),
        E201(201, "Token invalid"),
        E202(202, "Header invalid"),
        E203(203, "Data invalid(validate input)"),
        E204(204, "Data invalid(db raise, business error)"),
        E205(205, "Application error");
        private int code;
        private String name;

        STATUS(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public static final String DATE_DEFAULT_FORMAT = "dd/MM/yyyy";
    public static final String YEAR_DEFAULT_FORMAT = "yyyy";
    public static final String pwdKey = "PWD_ENCRYPTED";
    public static final String pwdSaltKey = "PWD_SALT";
//    public static final String pwdKey = "@v@905e2f031832cbe87e6d93344ecfFE85!0";
//    public static final String pwdSaltKey = "@aa1df3194fb61c48d14ae095ec580480";
    public static final String VTMAN_KEY = "key/EvtpPrivate.pem";


    //hubsub
    public final static String ContractPhotos = "CONTRACTPHOTOS";
    public final static String HubPhotos = "HUBPHOTOS";
    public final static String type = "COPY";

//    public final static String PATH = "localhost:9056";
//    public final static String PATH = "dev-2.viettelpost.vn";
//    public final static String PATH = "staging-vtman2.viettelpost.vn";
    public final static String PATH = "appv2.viettelpost.vn";
    public final static String DINH_DANG_FILE_KHONG_CHO_PHEP = "Định dạng file không cho phép";

    // noti
    public static final String APPLICATION_JSON = "application/json";
    public static final String PUSH_API_ENDPOINT = "https://app.viettelpost.vn/v1/evtmannotify";
    public enum NotificationChannel {
        PUSH("NOTIFICATION");

        private final String value;

        NotificationChannel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // API Sync
    public final static String API_PROVINCE = "https://location.okd.viettelpost.vn/location/v1.0/place?type=PROVINCE&size=100&system=VTP";
    public final static String API_DISTRICT = "https://location.okd.viettelpost.vn/location/v1.0/place?type=DISTRICT&size=10000&system=VTP";
    public final static String API_WARD = "https://location.okd.viettelpost.vn/location/v1.0/place?type=WARD&size=100000&system=VTP";
    public final static String API_STREET = "https://location.okd.viettelpost.vn/location/v1.0/autocomplete?system=VTP&ctx=SUBWARD&ctx={wardId}";

    public final static String QUERY_POSTOFFICE = "SELECT BUUCUC, TINH, QUANHUYEN, PHUONGXA, NAME, LATITUDE, LONGITUDE, PHONE FROM VTP.DM_BUUCUCQUANLYVIEW";

    public static final String QUERY_BUUCUC = "SELECT DEPTCODE, TEN_BUUCUC, MA_TINH, CAP_BUUCUC, SU_DUNG, VUNG," +
            "MA_BUUCUC, ID_BUUCUC FROM VTP.DM_BUUCUC";

    //Page
//    public final static int PAGE_SIZE = 10;

    // Error
//    public final static ErrorMessage POST_OFFICE_NOT_EXITS = new ErrorMessage("ERR_001", "Bưu cục không tồn tại");
//    public final static ErrorMessage NOT_PERMISSION = new ErrorMessage("ERR_002", "Không có quyền");
//    public final static ErrorMessage ERROR_FORMAT = new ErrorMessage("ERR_003", "Số điện thoại không đúng định dạng");
//    public final static ErrorMessage PHONE_USED = new ErrorMessage("ERR_004", "Số điện thoại đã được sử dụng");
//    public final static ErrorMessage NOT_FIND_PROVINCE = new ErrorMessage("ERR_005", "Không tìm thấy tỉnh");
//    public final static ErrorMessage NOT_FIND_DISTRICT = new ErrorMessage("ERR_006", "Không tìm thấy quận/huyện");
//    public final static ErrorMessage NOT_FIND_WARD = new ErrorMessage("ERR_007", "Không tìm thấy phường/xã");
//    public final static ErrorMessage NOT_FIND_STREET = new ErrorMessage("ERR_008", "Không tìm thấy đường/thôn/xóm");
//    public final static ErrorMessage HUBSUB_NOT_EXITS = new ErrorMessage("ERR_009", "Hub Sub không tồn tại");
//    public final static ErrorMessage STATUS_APPROVE = new ErrorMessage("ERR_010", "Chỉ trạng thái Chờ phê duyệt mới được phê duyệt");
//    public final static ErrorMessage STATUS_REQUEST_APPROVED = new ErrorMessage("ERR_011", "Đã được yêu cầu phê duyệt");
//    public final static ErrorMessage PICK_ACTION = new ErrorMessage("ERR_012", "Chưa chọn action. Action phải là AGREE OR NOT-AGREE");
//    public final static ErrorMessage EMPTY_REASON = new ErrorMessage("ERR_013", "Lý do không được để trống");
//    public final static ErrorMessage DIF_APPROVE_REQUEST = new ErrorMessage("ERR_014", "Trạng thái mới tạo và chưa gửi duyệt mới xóa được");

    // Action
    public final static String ACTION_REQUEST_APPROVE = "REQUEST-APPROVE";
    public final static String ACTION_AGREE = "AGREE";
    public final static String ACTION_NOT_AGREE = "NOT-AGREE";

    //app id
    public final static String APP_CODE = "EVTP2";


}
