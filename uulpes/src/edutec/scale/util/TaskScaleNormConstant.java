package edutec.scale.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class TaskScaleNormConstant {

    private static final HashMap<String, String> NORMMAP = new HashMap<String, String>();
    private static List<NormObj> result = new ArrayList<NormObj>();
    private static final String SCALEID = "330000110";

    public TaskScaleNormConstant() {
        NORMMAP.put("rydx", "人员导向");
        NORMMAP.put("rwdx", "任务导向");
        result.add(new NormObj("0-7", "0-7", 4));
        result.add(new NormObj("0-7", "8-15", 3));
        result.add(new NormObj("8-15", "0-7", 2));
        result.add(new NormObj("8-15", "8-15", 1));
    }

    public int getTaskScaleNormResult(HashMap<String, Integer> paramMap) {
        int ryfs = paramMap.get("人员导向");
        int rwfs = paramMap.get("任务导向");
        String rydx = "";
        String rwdx = "";
        if (ryfs < 7) {
            rydx = "0-7";
        } else {
            rydx = "8-15";
        }
        if (rwfs < 7) {
            rwdx = "0-7";
        } else {
            rwdx = "8-15";
        }
        for (NormObj normObj : result) {
            if (normObj.getRydx().equals(rydx) && normObj.getRwdx().equals(rwdx)) {
                return normObj.getValue();
            }
        }
        return 0;
    }

    class NormObj {
        private String rydx;
        private String rwdx;
        private int value;

        public NormObj(String rydx, String rwdx, int value) {
            this.rwdx = rwdx;
            this.rydx = rydx;
            this.value = value;
        }

        public String getRydx() {
            return rydx;
        }

        public void setRydx(String rydx) {
            this.rydx = rydx;
        }

        public String getRwdx() {
            return rwdx;
        }

        public void setRwdx(String rwdx) {
            this.rwdx = rwdx;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static String getScaleid() {
        return SCALEID;
    }

}
