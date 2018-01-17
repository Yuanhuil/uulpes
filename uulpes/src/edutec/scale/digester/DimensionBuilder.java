package edutec.scale.digester;

import org.apache.commons.digester.Digester;

import edutec.scale.model.Dimension;
import edutec.scale.model.Formula;
import heracles.digester.DigesterUtils;

public class DimensionBuilder {
    private static final String ADD_DIMENSION = "addDimension";

    static public final String PATTEN_DIMENSION = "scale/dimensions/d";
    static public final String PATTEN_QUESTIONIDS = PATTEN_DIMENSION + "/questionIds";
    static public final String PATTEN_REVERSEQUESTIONIDS = PATTEN_DIMENSION + "/reverseQuestionIds";
    static public final String PATTEN_DIMENSIONIDS = PATTEN_DIMENSION + "/dimensionIds";

    static public final String PATTEN_FORMULA = "*/formula";

    static public final String[] PATTEN_DIMENSION_PROPS = { PATTEN_QUESTIONIDS, PATTEN_REVERSEQUESTIONIDS,
            PATTEN_DIMENSIONIDS };

    private Digester digester;

    static public DimensionBuilder newInstance(Digester digester) {
        DimensionBuilder factory = new DimensionBuilder();
        factory.digester = digester;
        return factory;
    }

    private DimensionBuilder() {

    }

    public void buildDimension() {
        DigesterUtils.add3(digester, PATTEN_DIMENSION, Dimension.class, ADD_DIMENSION);
        DigesterUtils.addBeanPropertySetters(digester, PATTEN_DIMENSION_PROPS);
        DigesterUtils.add3(digester, PATTEN_FORMULA, Formula.class, "setFormula");
        digester.addSetNestedProperties(PATTEN_FORMULA);
    }
}
