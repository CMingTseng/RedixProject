package redix.booxtown.model;

import java.util.List;

/**
 * Created by thuyetpham94 on 28/09/2016.
 */
public class SettingResult {
    private int code;
    private List<Setting> setting;

    public SettingResult(int code, List<Setting> setting) {
        this.code = code;
        this.setting = setting;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Setting> getSetting() {
        return setting;
    }

    public void setSetting(List<Setting> setting) {
        this.setting = setting;
    }
}
