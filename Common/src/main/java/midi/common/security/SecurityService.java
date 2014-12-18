package midi.common.security;

public interface SecurityService {

    void login(String userName, String password);

    void logout();
}
