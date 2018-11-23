package ee.ajapaik.android.data.util;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.Date;

import ee.ajapaik.android.data.Hyperlink;
import ee.ajapaik.android.util.Dates;

public abstract class Model implements Parcelable {
    protected static boolean canRead(JsonObject obj, String key) {
        JsonElement element = obj.get(key);

        return element != null && !element.isJsonNull();
    }

    protected static JsonArray readArray(JsonObject obj, String key) {
        JsonArray array = obj.getAsJsonArray(key);

        return (array != null) ? array : new JsonArray();
    }

    protected static boolean readBoolean(JsonObject obj, String key) {
        JsonElement element = obj.get(key);

        if(element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();

            if(primitive.isBoolean()) {
                return primitive.getAsBoolean();
            }

            if(primitive.isNumber()) {
                return primitive.getAsInt() == 1;
            }
        }

        return false;
    }

    protected static Date readDateTime(JsonObject obj, String key) {
        JsonElement element = obj.get(key);

        if(element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();

            if(primitive.isString()) {
                Date value = Dates.parse(primitive.getAsString());

                if(value != null) {
                    return value;
                }
            }
        }

        return null;
    }

    protected static Date readDate(JsonObject obj, String key) {
        JsonElement element = obj.get(key);
        if(element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if(primitive.isString()) {
                return Dates.parseDate(primitive.getAsString());
            }
        }
        return null;
    }

    protected static Hyperlink readHyperlink(JsonObject obj, String key) {
        return readHyperlink(obj, key, null);
    }

    protected static Hyperlink readHyperlink(JsonObject obj, String key, Hyperlink defaultValue) {
        JsonElement element = obj.get(key);

        if(element != null) {
            if(element.isJsonObject()) {
                return Hyperlink.parse(element.getAsJsonObject());
            }

            if(element.isJsonPrimitive()) {
                JsonPrimitive primitive = element.getAsJsonPrimitive();

                if(primitive.isString()) {
                    Uri uri = Uri.parse(primitive.getAsString());

                    if(uri != null) {
                        return new Hyperlink(uri, null);
                    }
                }
            }
        }

        return defaultValue;
    }

    protected static String readIdentifier(JsonObject obj, String key) {
        JsonElement element = obj.get(key);

        if(element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();

            if(primitive.isString()) {
                return primitive.getAsString();
            }

            if(primitive.isNumber()) {
                return primitive.toString();
            }
        }

        return null;
    }

    protected static int readInteger(JsonObject obj, String key) {
        JsonElement element = obj.get(key);

        if(element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();

            if(primitive.isNumber()) {
                return primitive.getAsInt();
            }
        }

        return 0;
    }

    protected static long readLong(JsonObject obj, String key) {
        JsonElement element = obj.get(key);

        if(element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();

            if(primitive.isNumber()) {
                return primitive.getAsLong();
            }
        }

        return 0;
    }

    protected static double readNumber(JsonObject obj, String key) {
        JsonElement element = obj.get(key);

        if(element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();

            if(primitive.isNumber()) {
                return primitive.getAsDouble();
            }
        }

        return 0.0;
    }

    protected static JsonObject readObject(JsonObject obj, String key) {
        JsonElement element = obj.get(key);

        return (element != null && element.isJsonObject()) ? element.getAsJsonObject() : null;
    }

    protected static String readString(JsonObject obj, String key) {
        return readString(obj, key, null);
    }

    protected static String readString(JsonObject obj, String key, String defaultValue) {
        JsonElement element = obj.get(key);

        if(element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();

            if(primitive.isString()) {
                return primitive.getAsString();
            }
        }

        return defaultValue;
    }

    protected static Uri readUri(JsonObject obj, String key) {
        return readUri(obj, key, null);
    }

    protected static Uri readUri(JsonObject obj, String key, Uri defaultValue) {
        String uri = readString(obj, key);
        Hyperlink hyperlink;

        if(uri != null) {
            return Uri.parse(uri);
        }

        if((hyperlink = readHyperlink(obj, key)) != null) {
            return hyperlink.getURL();
        }

        return defaultValue;
    }

    protected static void write(JsonObject obj, String key, int value) {
        obj.addProperty(key, value);
    }

    protected static void write(JsonObject obj, String key, double value) {
        obj.addProperty(key, value);
    }

    protected static void write(JsonObject obj, String key, String value) {
        if(value != null) {
            obj.addProperty(key, value);
        }
    }

    protected static void write(JsonObject obj, String key, Uri value) {
        if(value != null) {
            obj.addProperty(key, value.toString());
        }
    }

    protected static void write(JsonObject obj, String key, Hyperlink value) {
        if(value != null) {
            obj.add(key, value.getAttributes());
        }
    }

    protected static void write(JsonObject obj, String key, Date value) {
        if(value != null) {
            obj.addProperty(key, Dates.toString(value));
        }
    }

    protected static void write(JsonObject obj, String key, JsonArray value) {
        if(value != null) {
            obj.add(key, value);
        }
    }

    protected static void writeDDMMYYYYDate(JsonObject obj, String key, Date value) {
        if(value != null) {
            obj.addProperty(key, Dates.toDDMMYYYYString(value));
        }
    }

    protected static void write(JsonObject obj, String key, boolean value) {
        if(value) {
            obj.addProperty(key, true);
        }
    }

    public abstract JsonObject getAttributes();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getAttributes().toString());
    }

    @Override
    public String toString() {
        return getAttributes().toString();
    }

    public static abstract class Creator<T> implements Parcelable.Creator<T> {
        public T parse(JsonObject obj) {
            if(obj != null) {
                try {
                    return newInstance(obj);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        public T parse(String str) {
            if(str != null) {
                try {
                    JsonElement element = new JsonParser().parse(new JsonReader(new StringReader(str)));

                    if(element.isJsonObject()) {
                        return newInstance(element.getAsJsonObject());
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        public T createFromParcel(Parcel in) {
            try {
                JsonElement element = new JsonParser().parse(new JsonReader(new StringReader(in.readString())));

                if(element.isJsonObject()) {
                    return newInstance(element.getAsJsonObject());
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        public abstract T newInstance(JsonObject attributes);
    }
}
