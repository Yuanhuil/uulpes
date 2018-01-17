package com.njpes.www.entity.baseinfo.attr;

import com.njpes.www.entity.baseinfo.StudentWithBLOBs;
import com.njpes.www.entity.baseinfo.TeacherWithBLOBs;
import com.njpes.www.entity.baseinfo.enums.AcountTypeFlag;
import com.njpes.www.service.baseinfo.AttrDefineManagerImpl;
import com.njpes.www.service.baseinfo.StudentServiceI;
import com.njpes.www.service.baseinfo.StudentServiceImpl;
import com.njpes.www.service.baseinfo.TeacherServiceI;
import com.njpes.www.service.baseinfo.TeacherServiceImpl;
import com.njpes.www.utils.SpringContextHolder;
import heracles.util.UtilCollection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties({"optAttrs", "wordAttrs"})
public abstract class PropObject
  extends PropBean
{
  protected static Set<String> DEAFAULT_EXCULDEATTRSET = new HashSet();
  
  static
  {
    DEAFAULT_EXCULDEATTRSET.add("100");
    DEAFAULT_EXCULDEATTRSET.add("101");
    DEAFAULT_EXCULDEATTRSET.add("104");
  }
  
  public static PropObject createPropObject(int typeFlag)
  {
    PropObject propObject = null;
    if (AcountTypeFlag.student.getId() == typeFlag) {
      propObject = new StudentWithBLOBs();
    } else if (AcountTypeFlag.teacher.getId() == typeFlag) {
      propObject = new TeacherWithBLOBs();
    }
    return propObject;
  }
  
  protected List<FieldValue> attrs = null;
  protected Long id;
  protected String backGrand;
  
  public Long getId()
  {
    return this.id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public String getBackGrand()
  {
    return this.backGrand;
  }
  
  public void setBackGrand(String backGrand)
  {
    this.backGrand = backGrand;
    setPropsByStr(this.backGrand);
  }
  
  public void load()
  {
    loadAttrVals();
  }
  
  public void update(int typeflag)
  {
    saveAttrVals(typeflag);
  }
  
  public abstract Set<String> getExculdeAttr();
  
  protected abstract int[] getCateIds();
  
  protected abstract void saveExcludeAttrs()
    throws SQLException;
  
  protected abstract void loadProps(String paramString)
    throws SQLException;
  
  protected abstract Object getAttrObject();
  
  public void loadMetas()
  {
    if (this.attrs == null)
    {
      this.attrs = new ArrayList();
      int[] cateIds = getCateIds();
      List<FieldValue> lowGrade = new ArrayList();
      for (int i = 0; i < cateIds.length; i++)
      {
        AttrDefineManagerImpl attrDefineManagerImpl = (AttrDefineManagerImpl)SpringContextHolder.getBean("AttrDefineManagerImpl", 
          AttrDefineManagerImpl.class);
        
        Collection<AttrDefine> dataList = attrDefineManagerImpl.getForCate(cateIds[i]);
        for (AttrDefine attrDefine : dataList) {
          if ((attrDefine.getObjectIdentifier() >= 107L) && (attrDefine.getObjectIdentifier() < 200L))
          {
            lowGrade.add(new FieldValue(attrDefine));
          }
          else
          {
            if (attrDefine.getOrgId() == 0L) {
              this.attrs.add(new FieldValue(attrDefine));
            }
            if (getExculdeAttr().contains(attrDefine.getId())) {
              attrDefine.setEnableReq(true);
            }
          }
        }
      }
      this.attrs.addAll(lowGrade);
    }
  }
  
  private void loadAttrVals()
  {
    try
    {
      getAttrVals();
      propsToAttrs();
    }
    catch (SQLException localSQLException) {}
  }
  
  public void propsToAttrs()
  {
    loadMetas();
    
    Map<String, Object> attrVals = getProps();
    if (MapUtils.isEmpty(attrVals)) {
      return;
    }
    for (FieldValue fv : this.attrs)
    {
      Object o = attrVals.get(fv.getId());
      if (o != null) {
        fv.setValue(attrVals.get(fv.getId()).toString());
      }
    }
  }
  
  public void setAttrsValsWithLabel(Map<String, String> attrVals)
  {
    loadMetas();
    for (FieldValue fv : this.attrs)
    {
      String o = (String)attrVals.get(fv.getLabel());
      if (o != null) {
        fv.setValue(((String)attrVals.get(fv.getLabel())).toString());
      }
    }
  }
  
  private void saveAttrVals(int typeflag)
  {
    try
    {
      if (typeflag == AcountTypeFlag.student.getId())
      {
        StudentServiceI studentServcie = (StudentServiceI)SpringContextHolder.getBean("studentService", 
          StudentServiceImpl.class);
        String fldVars = concatFldVars();
        studentServcie.updateBjxx(getId().longValue(), fldVars);
      }
      if (typeflag == AcountTypeFlag.teacher.getId())
      {
        TeacherServiceI teacherServcie = (TeacherServiceI)SpringContextHolder.getBean("teacherService", 
          TeacherServiceImpl.class);
        String fldVars = concatFldVars();
        teacherServcie.updateBjxx(getId().longValue(), fldVars);
      }
      saveExcludeAttrs();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private String concatFldVars()
  {
    StringBuilder sb = new StringBuilder();
    Set<String> exAttr = getExculdeAttr();
    for (FieldValue fv : this.attrs)
    {
      sb.append(fv.getId());
      sb.append('=');
      String str = removeInvalidateCharOfVal(fv);
      sb.append(str);
      sb.append(' ');
    }
    return sb.toString();
  }
  
  private String removeInvalidateCharOfVal(FieldValue fv)
  {
    String str = fv.getValue();
    str = StringUtils.remove(str, '=');
    str = StringUtils.remove(str, '&');
    str = StringUtils.deleteWhitespace(str);
    return str;
  }
  
  private Map<String, Object> getAttrVals()
    throws SQLException
  {
    loadProps(getBackGrand());
    
    return getProps();
  }
  
  public List<FieldValue> getAttrs()
  {
    return this.attrs;
  }
  
  @JsonProperty
  public List<FieldValue> getOptAttrs()
  {
    List<FieldValue> list = getAttrs();
    List<FieldValue> result = new ArrayList(list.size());
    for (FieldValue fieldValue : list) {
      if (!getExculdeAttr().contains(fieldValue.getId())) {
        result.add(fieldValue);
      }
    }
    return result;
  }
  
  public List<FieldValue> getFieldValues(String[] attrIds)
  {
    List<FieldValue> result = new ArrayList();
    for (FieldValue fieldValue : this.attrs) {
      if (ArrayUtils.contains(attrIds, fieldValue.getId())) {
        result.add(fieldValue);
      }
    }
    return result;
  }
  
  public Map<String, FieldValue> getWordAttrs(String[] attrIds)
  {
    Map<String, FieldValue> result = new LinkedHashMap();
    for (FieldValue field : this.attrs) {
      if (ArrayUtils.contains(attrIds, field.getId())) {
        if ((field.getType().equalsIgnoreCase(Field.FLD_TYPE_TEXT)) || 
          (field.getType().equalsIgnoreCase(Field.FLD_TYPE_RANGE)) || 
          (field.getType().equalsIgnoreCase(Field.FLD_TYPE_DATE)))
        {
          result.put(field.getId(), field);
        }
        else
        {
          AttrDefine attrDefine = (AttrDefine)field.getField();
          String optText = "";
          Map<String, String> map = attrDefine.getOptionMap();
          if (StringUtils.isNotEmpty(field.getValue())) {
            optText = (String)map.get(field.getValue());
          }
          FieldValue tmp = new FieldValue(attrDefine);
          tmp.setValue(optText);
          result.put(field.getId(), tmp);
        }
      }
    }
    return result;
  }
  
  public Map<String, FieldValue> getWordAttrs()
  {
    Map<String, FieldValue> result = new LinkedHashMap();
    for (FieldValue field : this.attrs) {
      if (!getExculdeAttr().contains(field.getId())) {
        if ((field.getType().equalsIgnoreCase(Field.FLD_TYPE_TEXT)) || 
          (field.getType().equalsIgnoreCase(Field.FLD_TYPE_RANGE)) || 
          (field.getType().equalsIgnoreCase(Field.FLD_TYPE_DATE)))
        {
          result.put(field.getId(), field);
        }
        else
        {
          AttrDefine attrDefine = (AttrDefine)field.getField();
          String optText = "";
          Map<String, String> map = attrDefine.getOptionMap();
          if (StringUtils.isNotEmpty(field.getValue())) {
            optText = (String)map.get(field.getValue());
          }
          FieldValue tmp = new FieldValue(attrDefine);
          tmp.setValue(optText);
          result.put(field.getId(), tmp);
        }
      }
    }
    return result;
  }
  
  public Map<String, String> getValueDescns(String[] attrIds)
  {
    Map<String, String> result = new LinkedHashMap();
    loadAttrDescToMap(attrIds, result);
    return result;
  }
  
  public void loadAttrDescToMap(String[] attrIds, Map<String, String> result)
  {
    result.clear();
    for (FieldValue field : this.attrs) {
      if (ArrayUtils.contains(attrIds, field.getId())) {
        if ((field.getType().equalsIgnoreCase(Field.FLD_TYPE_TEXT)) || 
          (field.getType().equalsIgnoreCase(Field.FLD_TYPE_RANGE)) || 
          (field.getType().equalsIgnoreCase(Field.FLD_TYPE_DATE)))
        {
          result.put(field.getId(), field.getValue());
        }
        else
        {
          AttrDefine attrDefine = (AttrDefine)field.getField();
          String optText = "";
          Map<String, String> map = attrDefine.getOptionMap();
          if (StringUtils.isNotEmpty(field.getValue())) {
            optText = (String)map.get(field.getValue());
          }
          result.put(field.getId(), optText);
        }
      }
    }
  }
  
  public void setAttrs(List<FieldValue> fldValueList)
  {
    this.attrs = fldValueList;
  }
  
  public void setPropsByStr(String str)
  {
    if (StringUtils.isEmpty(str)) {
      return;
    }
    Map<String, String> ss = UtilCollection.toMap(str, ' ', '=');
    setProps(ss);
  }
  
  public void loadBackGrandToMap(Map<String, String> map)
  {
    map.clear();
    Map<String, Object> props = getProps();
    if (props == null) {
      return;
    }
    for (Map.Entry<String, Object> ent : props.entrySet()) {
      if (!getExculdeAttr().contains(ent.getKey()))
      {
        Object o = ent.getValue();
        AttrDefineManagerImpl attrDefineManagerImpl = (AttrDefineManagerImpl)SpringContextHolder.getBean("AttrDefineManagerImpl", 
          AttrDefineManagerImpl.class);
        AttrDefine attrDefine = attrDefineManagerImpl.getForId(Integer.valueOf((String)ent.getKey()).intValue());
        String val = o.toString();
        if ((attrDefine.getType().equalsIgnoreCase(Field.FLD_TYPE_SELECT)) || 
          (attrDefine.getType().equalsIgnoreCase(Field.FLD_TYPE_YESNO))) {
          val = (String)attrDefine.getOptionMap().get(val);
        }
        map.put((String)ent.getKey(), val);
      }
    }
  }
  
  protected List<String> getExculudeParamVals()
  {
    List<String> result = new ArrayList(DEAFAULT_EXCULDEATTRSET.size());
    List<FieldValue> list = getAttrs();
    for (FieldValue fieldValue : list) {
      if (DEAFAULT_EXCULDEATTRSET.contains(fieldValue.getId()))
      {
        result.add(fieldValue.getValue());
        if (result.size() == DEAFAULT_EXCULDEATTRSET.size()) {
          break;
        }
      }
    }
    return result;
  }
}
