package ee.ajapaik.android.util;

public class Size {
    public final int width;
    public final int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean empty() {
        return width <= 0 && height <= 0;
    }

    @Override
    public boolean equals(Object obj) {
        Size size = (Size)obj;

        if(size == this) {
            return true;
        }

        return size != null && size.width == width && size.height == height;
    }

    @Override
    public String toString() {
        return "(" + Integer.toString(width) + ", " + Integer.toString(height) + ")";
    }
}
