package sg.edu.nus.qac_android.data.entity;

/**
 * @Author: Cooper
 * @Date: 3/9/2025
 * @Description:
 */
public class Auth0User {
    private final String uuid;
    private final String nickname;
    private final String email;
    private final String name;
    private final String sub;

    public Auth0User(String uuid, String nickname, String email, String name, String sub) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.email = email;
        this.name = name;
        this.sub = sub;
    }

    public String getUuid() {
        return uuid;
    }

    public String getNickname() {
        if (sub.startsWith("auth0")) {
            return nickname;
        }
        else {
            return name;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSub() {
        return sub;
    }

    @Override
    public String toString() {
        return "Auth0User{" +
                "uuid='" + uuid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", sub='" + sub + '\'' +
                '}';
    }
}
