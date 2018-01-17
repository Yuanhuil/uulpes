package heracles.util;

public abstract class ArrayIntClosure implements IntClosure {
    private int index;
    private int arrayLength;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getArrayLength() {
        return arrayLength;
    }

    public void setArrayLength(int arrayLength) {
        this.arrayLength = arrayLength;
    }

}
