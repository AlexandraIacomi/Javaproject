package ro.foame.employee_backend.controllers.client;


import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.stereotype.Component;

@Component
public class PayPalClient {

    private final PayPalHttpClient client;

    public PayPalClient() {
        PayPalEnvironment environment = new PayPalEnvironment.Sandbox(
                "AVdAlYLllK-clNDME-7oJS-2tpRxrRixQKPDWzhL5onPROCIJMV9RuW12Bf4Yc7Jp-yl0kUKcSIJN5hh",
                "EI8DiWmODwjzuO9GG-kGaV3t2Iw-TL-uLZq_pNWJNqxCJabEhj1oGFEOrkZf9UfcB_QM-Q3fkpAA6e_t"
        );
        client = new PayPalHttpClient(environment);
    }

    public PayPalHttpClient client() {
        return client;
    }
}