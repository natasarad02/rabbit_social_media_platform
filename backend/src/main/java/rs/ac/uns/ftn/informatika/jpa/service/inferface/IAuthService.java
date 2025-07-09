package rs.ac.uns.ftn.informatika.jpa.service.inferface;
import rs.ac.uns.ftn.informatika.jpa.dto.util.UserTokenState;

public interface IAuthService {
    UserTokenState login(String username, String password);
}
