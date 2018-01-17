package heracles.domain.model;

public class IdentifiableSupport implements Identifiable {
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof IdentifiableSupport))
            return false;

        final Identifiable object = (Identifiable) o;

        if (objectIdentifier == 0)
            return this == o;

        if (objectIdentifier != object.getObjectIdentifier())
            return false;

        return true;
    }

    private long objectIdentifier;

    public long getObjectIdentifier() {
        return objectIdentifier;
    }

    public void setObjectIdentifier(long objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(objectIdentifier).hashCode();
    }

    @Override
    public String toString() {
        return objectIdentifier + "";
    }

}
