package edutec.scale.model;

import java.util.Map;

import heracles.util.UtilCollection;

public abstract class TitleableSupport implements Titleable {
    public long getObjectIdentifier() {
        return 0;
    }

    public void setObjectIdentifier(long l) {

    }

    public Map<String, String> getDescnProps() {
        return UtilCollection.toMap(getDescn(), UtilCollection.SEMICOLON, UtilCollection.COLON);
    }
}
