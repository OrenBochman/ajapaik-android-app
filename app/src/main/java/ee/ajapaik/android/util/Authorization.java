package ee.ajapaik.android.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

import static ee.ajapaik.android.util.Authorization.Type.ANONYMOUS;

public class Authorization {

    private static final String KEY_TYPE = "type";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";

    public static Authorization getAnonymous() {
        return new Authorization(ANONYMOUS, null, null);
    }

    public static Authorization parse(String str) {
        if(str != null) {
            try {
                JsonElement element = new JsonParser().parse(new JsonReader(new StringReader(str)));

                if(element.isJsonObject()) {
                    return new Authorization(element.getAsJsonObject());
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private Type m_type;
    private String m_username;
    private String m_password;
    private String m_token;
    private String m_firstname;
    private String m_lastname;

    public Authorization(JsonObject attributes) {
        JsonPrimitive primitive;

        m_type = Type.parse(((primitive = attributes.getAsJsonPrimitive(KEY_TYPE)) != null && primitive.isNumber()) ? primitive.getAsInt() : Type.UNKNOWN.getCode());
        m_username = ((primitive = attributes.getAsJsonPrimitive(KEY_USERNAME)) != null && primitive.isString()) ? primitive.getAsString() : null;
        m_password = ((primitive = attributes.getAsJsonPrimitive(KEY_PASSWORD)) != null && primitive.isString()) ? primitive.getAsString() : null;
        m_token = ((primitive = attributes.getAsJsonPrimitive(KEY_TOKEN)) != null && primitive.isString()) ? primitive.getAsString() : null;
        m_firstname = ((primitive = attributes.getAsJsonPrimitive(KEY_FIRSTNAME)) != null && primitive.isString()) ? primitive.getAsString() : null;
        m_lastname = ((primitive = attributes.getAsJsonPrimitive(KEY_LASTNAME)) != null && primitive.isString()) ? primitive.getAsString() : null;
    }

    public Authorization(Type type, String username, String password, String firstname, String lastname) {
        this.m_type = type;
        this.m_username = username;
        this.m_password = password;
        this.m_firstname = firstname;
        this.m_lastname = lastname;
    }

    public Authorization(Type type, String username, String password) {
        this(type, username, password, null, null);
    }

    public JsonObject getAttributes() {
        JsonObject attributes = new JsonObject();

        attributes.addProperty(KEY_TYPE, m_type.getCode());

        if(m_username != null) {
            attributes.addProperty(KEY_USERNAME, m_username);
        }

        if(m_password != null) {
            attributes.addProperty(KEY_PASSWORD, m_password);
        }

        if(m_token != null) {
            attributes.addProperty(KEY_TOKEN, m_token);
        }

        if(m_firstname != null) {
            attributes.addProperty(KEY_FIRSTNAME, m_firstname);
        }

        if(m_lastname != null) {
            attributes.addProperty(KEY_LASTNAME, m_lastname);
        }

        return attributes;
    }

    public Type getType() {
        return m_type;
    }

    public String getUsername() {
        return m_username;
    }

    public String getPassword() {
        return m_password;
    }

    public String getToken() {
        return m_token;
    }

    public String getFirstname() {
        return m_firstname;
    }

    public String getLastname() {
        return m_lastname;
    }

    public boolean isAnonymous() {
        return ANONYMOUS.equals(m_type);
    }

    @Override
    public boolean equals(Object obj) {
        Authorization authorization = (Authorization)obj;

        if(authorization == this) {
            return true;
        }

        return authorization != null &&
                authorization.m_type == m_type &&
                Objects.match(authorization.m_username, m_username) &&
                Objects.match(authorization.m_password, m_password) &&
                Objects.match(authorization.m_token, m_token);
    }

    @Override
    public String toString() {
        return getAttributes().toString();
    }

    public enum Type {
        ANONYMOUS(0, "auto"),
        FACEBOOK(1, "fb"),
        GOOGLE(2, "google"),
        USERNAME_PASSWORD(3, "ajapaik"),
        UNKNOWN(-1, null);

        private final int m_code;
        private final String m_name;

        public static Type parse(int code) {
            for(Type type : values()) {
                if(type.getCode() == code) {
                    return type;
                }
            }

            return UNKNOWN;
        }

        Type(int code, String name) {
            m_code = code;
            m_name = name;
        }

        public int getCode() {
            return m_code;
        }

        public String getName() {
            return m_name;
        }
    }
}
