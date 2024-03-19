import org.springframework.web.bind.annotation.RestController;
import org.zheleznikov.generated.model.AuthResponse;


@RestController
public class TestController {

    public void test() {
        AuthResponse res = new AuthResponse();
    }
}
