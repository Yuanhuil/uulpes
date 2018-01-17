package edutec.scale.db;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.entity.scaletoollib.ScaleFilterParam;
import com.njpes.www.service.scaletoollib.ScaleMgrService;

import edutec.exception.QueryException;
import edutec.scale.db.CachedScaleMgr.CachedScale;
import edutec.scale.digester.ScaleBuilder;
import edutec.scale.model.Dimension;
import edutec.scale.model.Scale;
import edutec.scale.util.ScaleConstants;
import edutec.scale.util.ScaleUtils;
import heracles.util.LogUtils;
import heracles.web.config.ApplicationConfiguration;
import jodd.io.StringInputStream;

@Service("CachedScaleMgr")
public class CachedScaleMgr implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // 缓存中最多放64个量表
    private static final int CACHE_SIZE = 64;
    static private final Log logger = LogFactory.getLog(CachedScaleMgr.class);
    // private static CachedScaleMgr singleTon = new CachedScaleMgr();
    private String scalePath;
    private Map<String, CachedScale> scaleMap;
    private Set<String> alreadyLoadScales;

    private ScaleMgrService scaleMgrService;
    static final Lock lock = new ReentrantLock();

    // public static CachedScaleMgr getInstance() {
    // return singleTon;
    // }

    @Autowired
    public CachedScaleMgr(ScaleMgrService scaleMgrService) {
        try {
            this.scaleMgrService = scaleMgrService;
            // setupScaleXmlPath();
            // logger.info("find path of scale xml:" + scalePath + "...");
            scaleMap = new LinkedHashMap<String, CachedScale>();
            alreadyLoadScales = new HashSet<String>();
            loadAllScales();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    private void setupScaleXmlPath() {
        scalePath = ApplicationConfiguration.getInstance().makeWorkSubDir(ScaleConstants.SCALE_XML_DIR)
                .getAbsolutePath();
    }

    public Scale get(String key) {
        return this.get(key, false);
    }

    public List<Dimension> getDimensions(String scaleId) {
        Scale scale = get(scaleId, true);
        if (scale != null) {
            return scale.getRootDimensions();
        }
        return Collections.emptyList();
    }

    public Scale get(String key, boolean isLoad) {
        try {
            lock.lock();
            CachedScale cachedScale = scaleMap.get(key);
            if (cachedScale != null) {
                if (isLoad) {
                    cachedScale.accessCount++;
                    LogUtils.debug(logger, key + " count:" + cachedScale.accessCount);
                }
                if (isLoad && !cachedScale.isLoad) {
                    if (isFull())
                        prune();
                    // 这个地方开始从xml中完善scale
                    loadScaleFromXml(cachedScale.scale);
                    cachedScale.isLoad = true;
                }
                return cachedScale.scale;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    private void prune() {
        LogUtils.debug(logger, "prune cached scale!");
        CachedScale first = null;
        Iterator<CachedScale> values = scaleMap.values().iterator();
        while (values.hasNext()) {
            CachedScale obj = values.next();
            if (first == null) {
                first = obj;
            } else if (first.accessCount > obj.accessCount) {
                first = obj;
            }
        }
        if (first != null) {
            while (values.hasNext()) {
                CachedScale obj = values.next();
                obj.accessCount -= first.accessCount;
                if (obj.accessCount <= 0) {
                    obj.isLoad = false;
                    obj.scale.clear();
                    alreadyLoadScales.remove(obj.scale.getId());
                }
            }
        }
    }

    private boolean isFull() {
        return alreadyLoadScales.size() >= CACHE_SIZE;
    }

    private void loadAllScales() throws QueryException {
        logger.info("start loadAllScales...");
        List<Scale> list = scaleMgrService.getSimpleScales();
        for (Scale scale : list) {
            // huangc,将所有量表相关的信息全部放入到内存中去
            loadScaleFromXml(scale);
            scaleMap.put(scale.getCode(), new CachedScale(scale));
        }
        logger.info("loadAllScales ok!");
    }

    private void loadScaleFromXml(Scale scale) {
        // 优先判断是否是自定义量表,自定义的量表都取自数据库的xmlstr字段中;
        InputStream input = null;
        // if ((scale.getFlag() & ScaleUtils.SCALE_CUST_FLAG) > 0) {

        try {
            String xmlStr = scaleMgrService.getXmlStr(scale.getCode());
            // if(scale.getCode().equals("110001101")){
            // System.out.println("s");
            // }
            xmlStr = xmlStr.replaceAll("&", "&amp;");
            input = new StringInputStream(xmlStr);
            ScaleBuilder.doBuild(input, scale);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(input);
        }
        alreadyLoadScales.add(scale.getId());
        return;
        // }
        /*
         * String xmlfile = null; if ((scale.getFlag() &
         * ScaleUtils.SCALE_CEE_FLAG) > 0) { // 高考量表表取自cee目录 xmlfile =
         * FilenameUtils.concat(scalePath, "cee"); } else if ((scale.getFlag() &
         * ScaleUtils.SCALE_PROPER_FLAG) > 0) { // 产权量表取自prop目录 xmlfile =
         * FilenameUtils.concat(scalePath, "prop");
         * 
         * } else { xmlfile = FilenameUtils.concat(scalePath, "others"); }
         * xmlfile = FilenameUtils.concat(xmlfile, scale.getId() + ".xml"); try
         * { input = new FileInputStream(xmlfile); //开始根据流构建scale对象
         * ScaleBuilder.doBuild(input, scale); } catch (Exception e) {
         * logger.error(e.getMessage(), e); } finally {
         * IOUtils.closeQuietly(input); } alreadyLoadScales.add(scale.getId());
         */
    }

    public void addScale(Scale scale, boolean isLoad) {
        try {
            lock.lock();
            if (isFull())
                prune();
            scaleMap.put(scale.getCode(), new CachedScale(scale, isLoad));
        } finally {
            lock.unlock();
        }
    }

    public void removeScale(Scale scale) {
        try {
            lock.lock();
            scaleMgrService.deleteScale(scale.getId());
            scaleMap.remove(scale.getId());
            alreadyLoadScales.remove(scale.getId());
            scale = null;
        } finally {
            lock.unlock();
        }
    }

    public void refresh() {
        try {
            lock.lock();
            Collection<CachedScale> values = this.scaleMap.values();
            for (CachedScale cachedScale : values) {
                cachedScale.scale.clear();
            }
            scaleMap.clear();
            alreadyLoadScales.clear();
            loadAllScales();
        } catch (QueryException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void refresh(String code) {
        try {
            lock.lock();
            CachedScale cachedScale = scaleMap.get(code);
            Scale scale = scaleMgrService.getScaleByCode(code);
            cachedScale.scale.setShowtitle(scale.getShowtitle());
            scaleMap.put(code, cachedScale);
        } catch (QueryException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public List<Scale> getScaleList() {
        try {
            lock.lock();
            List<Scale> result = new ArrayList<Scale>(scaleMap.size());
            for (CachedScale cachedScale : scaleMap.values()) {
                result.add(cachedScale.scale);
            }
            return result;
        } finally {
            lock.unlock();
        }
    }

    public List<Scale> getScaleList(long orgId) {
        List<Scale> list = getScaleList();
        List<Scale> result = new ArrayList<Scale>();
        for (Scale scale : list) {
            if (scale.getOrgId() == orgId || scale.getOrgId() == 0) {
                result.add(scale);
            }
        }
        return result;
    }

    public List<Scale> getScaleList(int flag, long orgId) {
        List<Scale> list = getScaleList(orgId);
        List<Scale> result = new ArrayList<Scale>();
        for (Scale scale : list) {
            if ((scale.getFlag() & flag) == flag) {
                result.add(scale);
            }
        }
        return result;
    }

    public List<Scale> getScaleList(int flag, long orgId, String[] exclude) {
        List<Scale> list = getScaleList(orgId);
        List<Scale> result = new ArrayList<Scale>();
        for (Scale scale : list) {
            if ((scale.getFlag() & flag) == flag) {
                boolean isFound = ArrayUtils.contains(exclude, scale.getId());
                if (!isFound) {
                    result.add(scale);
                }
            }
        }
        return result;
    }

    public List<Scale> getScaleList(int flag) {
        List<Scale> list = getScaleList();
        List<Scale> result = new ArrayList<Scale>();
        for (Scale scale : list) {
            if ((scale.getFlag() & flag) > 0) {
                result.add(scale);
            }
        }
        return result;
    }

    public List<Scale> getScaleListByCategory(int category) {
        List<Scale> list = getScaleList();
        List<Scale> result = new ArrayList<Scale>();
        for (Scale scale : list) {
            if ((scale.getCategory() & category) == category) {
                result.add(scale);
            }
        }
        return result;
    }

    public List<Scale> getScaleListOrderByStarlevel() {
        return getScaleListOrderBy(new StrartLevelComparator());
    }

    public List<Scale> getScaleListOrderByPriority() {
        return getScaleListOrderBy(new PriorityComparator());
    }

    private List<Scale> getScaleListOrderBy(Comparator<Scale> c) {
        List<Scale> list = getScaleList(ScaleUtils.SCALE_FREE_FLAG);
        List<Scale> result = new ArrayList<Scale>(list);
        Collections.sort(result, c);
        return result;
    }

    class StrartLevelComparator implements Comparator<Scale> {
        public int compare(Scale o1, Scale o2) {
            return o1.getStarlevel() - o2.getStarlevel();
        }
    }

    class PriorityComparator implements Comparator<Scale> {
        public int compare(Scale o1, Scale o2) {
            return o1.getPriority() - o2.getPriority();
        }
    }

    class CachedScale implements Serializable {
        Scale scale;
        volatile boolean isLoad = false;
        volatile int accessCount = 0;

        public CachedScale(Scale scale) {
            this.scale = scale;
        }

        public CachedScale(Scale scale, boolean isLoad) {
            super();
            this.scale = scale;
            this.isLoad = isLoad;
        }
    }

    public List<Scale> getFilterScaleList(ScaleFilterParam scaleFilterParam) {
        List<Scale> list = getScaleList();
        // 如果有量表来源搜索字段
        if (list.size() > 0 && (!scaleFilterParam.getScaleSourceId().equals("-1"))) {
            ListIterator<Scale> it = list.listIterator();
            while (it.hasNext()) {
                Scale scale = it.next();
                if (!(scale.getSource().equals(scaleFilterParam.getScaleSourceId()))) {
                    it.remove();
                }
            }
        }
        // 如果有量表类别字段
        if (list.size() > 0 && (!scaleFilterParam.getScaleTypeId().equals("-1"))) {
            ListIterator<Scale> it = list.listIterator();
            while (it.hasNext()) {
                Scale scale = it.next();
                if (!(scale.getScaleType().equals(scaleFilterParam.getScaleTypeId()))) {
                    it.remove();
                }
            }
        }
        // 如果有量表名称字段
        if (list.size() > 0 && (!scaleFilterParam.getScaleId().equals(""))) {
            ListIterator<Scale> it = list.listIterator();
            while (it.hasNext()) {
                Scale scale = it.next();
                if (!(scale.getTitle().toLowerCase().contains(scaleFilterParam.getScaleId().toLowerCase()))) {
                    it.remove();
                }
            }
        }
        // 如果有量表适用人群字段
        if (list.size() > 0 && (!scaleFilterParam.getApplicablePerson().equals("-1"))) {
            ListIterator<Scale> it = list.listIterator();
            while (it.hasNext()) {
                Scale scale = it.next();
                String applicablePerson = scale.getApplicablePerson();
                String xdStr = scaleFilterParam.getApplicablePerson();
                boolean condition = true; // 判断条件
                String[] split = StringUtils.split(xdStr, ",");
                for (String xd : split) {
                    // 循环判断量表适应人群是否包含当前学校的学段,只要包含一个就不remove.
                    if (StringUtils.contains(applicablePerson, xd)) {
                        condition = false;
                        break;
                    }
                }
                if (condition) {
                    it.remove();
                }
            }
        }
        // 如果有是否预警字段
        if (list.size() > 0 && (!scaleFilterParam.getIsWarn().equals("-1"))) {
            ListIterator<Scale> it = list.listIterator();
            while (it.hasNext()) {
                Scale scale = it.next();
                if (!(String.valueOf(scale.isWarningOrNot()).equals(scaleFilterParam.getIsWarn()))) {
                    it.remove();
                }
            }
        }
        return list;
    }
}
