package com.njpes.www.service.consultcenter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.njpes.www.dao.consultcenter.SchedulingMapper;
import com.njpes.www.entity.consultcenter.Scheduling;
import com.njpes.www.utils.PageParameter;

@Service("schedulingService")
public class SchedulingServiceImpl implements SchedulingServiceI {
    @Autowired
    private SchedulingMapper schedulingMapper;
    @Autowired
    private PlatformTransactionManager txManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.SchedulingServiceI#updateScheduling
     * (com.njpes.www.entity.consultcenter.Scheduling)
     */
    @Override
    public int updateScheduling(Scheduling scheduling) {
        // TODO Auto-generated method stub
        return schedulingMapper.updateByPrimaryKey(scheduling);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.SchedulingServiceI#saveScheduling
     * (com.njpes.www.entity.consultcenter.Scheduling)
     */
    @Override
    public int saveScheduling(Scheduling scheduling) {
        // TODO Auto-generated method stub
        return schedulingMapper.insert(scheduling);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.SchedulingServiceI#delScheduling(
     * com.njpes.www.entity.consultcenter.Scheduling)
     */
    @Override
    public int delScheduling(Scheduling scheduling) {
        // TODO Auto-generated method stub
        return schedulingMapper.deleteByPrimaryKey(scheduling.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.SchedulingServiceI#selectByPrimaryKey
     * (long)
     */
    @Override
    public Scheduling selectByPrimaryKey(long id) {
        // TODO Auto-generated method stub
        return schedulingMapper.selectByPrimaryKey(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.SchedulingServiceI#selectListByEntity
     * (com.njpes.www.entity.consultcenter.Scheduling,
     * com.njpes.www.utils.PageParameter)
     */
    @Override
    public List<Scheduling> selectListByEntity(Scheduling scheduling, PageParameter page, Date beginDate,
            Date endDate) {
        // TODO Auto-generated method stub
        return schedulingMapper.selectListByEntity(scheduling, page, beginDate, endDate);
    }

    /**
     * 根据开始日期结束日期查出所有记录 增加应该增加的，删除无效的，
     */
    @Override
    public String saveScheduling(Scheduling scheduling, int startTimeId, int endTimeId, Date endDate, String dateType) {
        String str = "";
        Calendar calendar = Calendar.getInstance();
        Map<String, Long> timeIdMap = new HashMap<String, Long>();
        Date startDate = scheduling.getDate();
        scheduling.setId(null);
        scheduling.setDate(null);
        Date nextDate = null;
        if (endDate == null) {
            calendar.setTime(startDate);
            calendar.add(calendar.HOUR, 24);
            nextDate = calendar.getTime();
            endDate = startDate;
        } else {
            nextDate = endDate;
        }
        List<Scheduling> list = schedulingMapper.selectListByEntity(scheduling, null, startDate, nextDate);

        calendar.setTime(startDate);
        int week = calendar.get(calendar.DAY_OF_WEEK);
        for (Scheduling scheduling2 : list) {
            if (dateType.equals("everyWeek")) {
                calendar.setTime(scheduling2.getDate());
                if (calendar.get(calendar.DAY_OF_WEEK) != week) {
                    continue;
                }
            }
            timeIdMap.put(scheduling2.getTimeid() + scheduling2.getDate().getTime() + "", scheduling2.getId());
        }

        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);
        try {

            while (!startDate.after(endDate)) {
                for (int i = startTimeId; i < endTimeId; i++) {
                    calendar.setTime(startDate);
                    calendar.set(calendar.HOUR, startTimeId / 2);
                    calendar.set(calendar.MINUTE, (startTimeId % 2 == 1 ? 30 : 0));
                    Date newStartDate = calendar.getTime();
                    scheduling.setDate(newStartDate);
                    Long id = timeIdMap.get(i);
                    if (id == null) {
                        scheduling.setTimeid(i);
                        scheduling.setId(null);
                        schedulingMapper.insert(scheduling);

                    } else {

                        /*
                         * scheduling.setId(id); scheduling.setTimeid(i);
                         * schedulingMapper.updateByPrimaryKey(scheduling);
                         */
                        timeIdMap.remove(scheduling.getTimeid() + scheduling.getDate().getTime() + "");
                    }
                }
                if (dateType.equals("everyWeek")) {
                    calendar.setTime(startDate);
                    calendar.add(Calendar.DATE, 7);
                } else {
                    calendar.setTime(startDate);
                    calendar.add(Calendar.DATE, 1);
                }
                startDate = calendar.getTime();
            }
            for (String timeid : timeIdMap.keySet()) {
                schedulingMapper.deleteByPrimaryKey(timeIdMap.get(timeid));
            }
            txManager.commit(status); // 提交事务
            str = "成功";
        } catch (Exception e) {
            // 否则回滚
            e.printStackTrace();
            txManager.rollback(status);
            str += "失败";
            return str;
        }
        return str;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.SchedulingServiceI#delByEntity(com
     * .njpes.www.entity.consultcenter.Scheduling)
     */
    @Override
    public int delByEntity(Scheduling scheduling) {
        // TODO Auto-generated method stub
        if (scheduling.getDate() != null && scheduling.getTeacherid() != null) {
            return schedulingMapper.delByEntity(scheduling);
        }
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.njpes.www.service.consultcenter.SchedulingServiceI#delByEntity(com
     * .njpes.www.entity.consultcenter.Scheduling, java.util.Date,
     * java.lang.String)
     */
    @Override
    public String delByEntity(Scheduling scheduling, Date endDate, String dateType) {
        String str = "";
        scheduling.setId(null);
        scheduling.setTimeid(null);

        Calendar calendar = Calendar.getInstance();
        Date startDate = scheduling.getDate();
        Date nextDate = null;
        int week = 0;
        calendar.setTime(startDate);
        week = calendar.get(calendar.DAY_OF_WEEK);
        if (endDate == null || dateType.equals("today")) {

            calendar.add(calendar.HOUR, 24);
            nextDate = calendar.getTime();
            endDate = startDate;
        } else {
            nextDate = endDate;
        }
        scheduling.setDate(null);
        List<Scheduling> list = schedulingMapper.selectListByEntity(scheduling, null, startDate, nextDate);

        // 在数据库中添加量表记录，要用事物一次性提交，所有操作数据库的地方全部放这里
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(def);

        try {
            Map<Long, Date> map = new HashMap<Long, Date>();
            for (Scheduling scheduling2 : list) {
                Date d = map.get(scheduling2.getDate().getTime());
                if (d == null) {
                    map.put(scheduling2.getDate().getTime(), scheduling2.getDate());
                    if (dateType.equals("everyWeek")) {
                        calendar.setTime(scheduling2.getDate());
                        if (calendar.get(calendar.DAY_OF_WEEK) == week) {
                            scheduling2.setId(null);
                            scheduling2.setTimeid(null);
                            int a = this.delByEntity(scheduling2);
                        }

                    } else {
                        scheduling2.setId(null);
                        scheduling2.setTimeid(null);
                        int a = this.delByEntity(scheduling2);
                    }
                }

            }
            txManager.commit(status); // 提交事务
            str = "成功";
        } catch (Exception e) {
            // 否则回滚
            e.printStackTrace();
            txManager.rollback(status);
            str += "失败";
            return str;
        }

        return str;
    }

    @Override
    public long getNextId(long id) {
        Scheduling scheduling = this.selectByPrimaryKey(id);
        if (scheduling == null) {
            return 0;
        }
        scheduling.setId(null);
        scheduling.setTimeid(scheduling.getTimeid() + 1);
        List<Scheduling> list = this.selectListByEntity(scheduling, new PageParameter(0, 10), null, null);
        if (list.size() > 0) {
            return list.get(0).getId();
        }
        return 0;
    }

}
