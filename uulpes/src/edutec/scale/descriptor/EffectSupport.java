package edutec.scale.descriptor;

import edutec.scale.model.Dimension;
import edutec.scale.questionnaire.DimDetail;
import heracles.util.Effect;

/**
 * 其作用于维度，主要做维度分数计算和维度解释
 * 
 * @author
 */
public abstract class EffectSupport implements Effect {
    public EffectSupport(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    public EffectSupport() {
    }

    protected Descriptor descriptor;

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * 提供维度的标准分和相应的解释
     */
    public void perform(Object o) {
        DimDetail dimDetail = (DimDetail) o;
        // 是否继续计算，个性倾向量表-29中说谎题;小学生学科兴趣量表
        if (dimDetail.getConstrain() != Dimension.NO_CALC)
            dimStdScore(dimDetail); /* 设置维度标准分 */
        dimExplain(dimDetail); /* 设置维度解释 */
    }

    /**
     * 目的是为维度获取解释
     * 
     * @param dimDetail
     */
    protected abstract void dimExplain(DimDetail dimDetail);

    /**
     * 目的是为维度获取标准分
     * 
     * @param dimDetail
     */
    protected abstract void dimStdScore(DimDetail dimDetail);

}
