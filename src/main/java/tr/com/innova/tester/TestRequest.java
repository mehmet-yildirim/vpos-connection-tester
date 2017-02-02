package tr.com.innova.tester;

import lombok.Data;
import lombok.ToString;

/**
 * Created by mehmet on 2.02.2017.
 */
@Data
@ToString
public class TestRequest {

    private String ccno;

    private String ccexp;

    private String cvv;

    private String merchantid;

    private String terminalid;

    private String termpasswd;
}
