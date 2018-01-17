package edutec.scale.model;

import java.util.Map;

import heracles.domain.model.Identifiable;

public interface Titleable extends Identifiable {
    String getTitle();

    String getDescn();

    public Map<String, String> getDescnProps();
}
