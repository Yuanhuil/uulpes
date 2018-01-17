package com.njpes.www.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.scaletoollib.StatConfigMapper;

@Service("StatUtilService")
public class StatUtilServiceImpl implements StatUtilServiceI {

    @Autowired
    StatConfigMapper tvalMapper;

    @Override
    public Float computeMean(List<Float> datas) {
        // TODO Auto-generated method stub
        Float mean = 0f;
        int count = datas.size();
        for (int i = 0; i < count; i++) {
            mean = mean + datas.get(i);
        }
        return mean / count;
    }

    @Override
    public Float computeSn(List<Float> datas) {
        // TODO Auto-generated method stub
        Float mean = computeMean(datas);
        Float sumSd = 0f;
        int count = datas.size();
        for (int i = 0; i < count; i++) {
            sumSd = sumSd + (datas.get(i) - mean) * (datas.get(i) - mean);
        }
        return Float.parseFloat(String.valueOf((Double.parseDouble(String.valueOf(sumSd / (count - 1))))));
    }

    @Override
    public Float computeIsd(List<Float> datas) {
        // TODO Auto-generated method stub
        Float mean = computeMean(datas);
        Float sumSd = 0f;
        int count = datas.size();
        for (int i = 0; i < count; i++) {
            sumSd = sumSd + (datas.get(i) - mean) * (datas.get(i) - mean);
        }
        return Float.parseFloat(String.valueOf((Double.parseDouble(String.valueOf(sumSd / count)))));
    }

    @Override
    public Float computeSumSd(List<Float> datas) {
        // TODO Auto-generated method stub
        Float mean = computeMean(datas);
        Float sumSd = 0f;
        int count = datas.size();
        List<Float> sumI = new ArrayList<Float>();
        for (int i = 0; i < count; i++) {
            sumSd = Math.abs(datas.get(i) - mean);
            sumI.add(sumSd);
        }
        Float sumIn = 0f;
        Float mean1 = computeMean(sumI);
        for(int i=0;i<sumI.size();i++){
            System.out.println(sumI.get(i));
            sumIn = sumIn + (sumI.get(i) - mean1) * (sumI.get(i) - mean1);
        }
        System.out.println(sumIn);
        return Float.parseFloat(String.valueOf((Double.parseDouble(String.valueOf(sumIn)))));
    }

    @Override
    public Float computeSumSdall(List<Float> datasO,List<Float> datasT) {
        // TODO Auto-generated method stub
        Float meanO = computeMean(datasO);
        Float meanT = computeMean(datasT);
        Float sumSd = 0f;
        int count = datasO.size();
        List<Float> sumI = new ArrayList<Float>();
        for (int i = 0; i < count; i++) {
            sumSd = Math.abs(datasO.get(i) - meanO);
            sumI.add(sumSd);
        }
        for (int j = 0;j < datasT.size();j++){
            sumSd = Math.abs(datasT.get(j) - meanT);
            sumI.add(sumSd);
        }
        Float sumIn = 0f;
        Float mean1 = computeMean(sumI);
        for(int i=0;i<sumI.size();i++){
            sumIn = sumIn + (sumI.get(i) - mean1) * (sumI.get(i) - mean1);
        }
        System.out.println(sumIn);
        return Float.parseFloat(String.valueOf((Double.parseDouble(String.valueOf(sumIn)))));
    }

    @Override
    public Float computeSd(List<Float> datas) {
        // TODO Auto-generated method stub
        Float mean = computeMean(datas);
        Float sumSd = 0f;
        int count = datas.size();
        for (int i = 0; i < count; i++) {
            sumSd = sumSd + ((datas.get(i) - mean) * (datas.get(i) - mean));
            System.out.println("维度分="+(datas.get(i) - mean));
            System.out.println("平方="+(datas.get(i) - mean)* (datas.get(i) - mean));
        }
        return Float.parseFloat(String.valueOf(Math.sqrt(Double.parseDouble(String.valueOf(sumSd / (count - 1))))));
    }

    @Override
    public Float computeCorelation(List<Float> datasX, List<Float> datasY) {
        // TODO Auto-generated method stub
        int countX = datasX.size();
        int countY = datasY.size();
        if (countX == countY) {
            Float meanX = computeMean(datasX);
            Float meanY = computeMean(datasY);
            Float sumDMeanXY = 0f;
            Float sumX2 = 0f;
            Float sumY2 = 0f;
            // 离均差x-meanx
            List<Float> dMeanX = new ArrayList<Float>();
            for (int i = 0; i < datasX.size(); i++) {
                sumDMeanXY = sumDMeanXY + (datasX.get(i) - meanX) * (datasY.get(i) - meanY);
                sumX2 = sumX2 + (datasX.get(i) - meanX) * (datasX.get(i) - meanX);
                sumY2 = sumY2 + (datasY.get(i) - meanY) * (datasY.get(i) - meanY);
            }
            Float sumD2XY = Float.parseFloat(Double.toString(Math.sqrt(sumX2 * sumY2)));
            // 分母为0,相关系数为0
            return sumD2XY == 0 ? 0 : sumDMeanXY / sumD2XY;
        }

        return null;
    }

    @Override
    public Float getTvalue(String t, Integer df) {
        // TODO Auto-generated method stub
        return tvalMapper.getTvalue(t, df);
    }

    @Override
    public Float getFvalue(int rOne, int rTwo) {
        // TODO Auto-generated method stub
        return tvalMapper.getFvalue(rOne, rTwo);
    }

    @Override
    public Float getPvalue(Float t) {
        // TODO Auto-generated method stub
        return tvalMapper.getPvalue(t);
    }

    @Override
    public Object[] merge(Object[] a, Object[] b) {
        // TODO Auto-generated method stub
        Set<Object> set = new TreeSet<Object>();
        set.addAll(Arrays.asList(a));
        set.addAll(Arrays.asList(b));
        return set.toArray();
    }
}
