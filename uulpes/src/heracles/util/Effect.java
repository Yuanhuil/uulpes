package heracles.util;

/**
 * 负责重新计算维度的分数或/和从数据库中获取文本 以后的版本此类仅做重新计算维度的分数，解释的获得只由 <code>ScaleExplain 来负责。
 * 
 * @author Administrator
 */
public interface Effect {
    void perform(Object o);
}
