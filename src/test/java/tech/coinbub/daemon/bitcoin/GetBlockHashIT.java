package tech.coinbub.daemon.bitcoin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tech.coinbub.daemon.Bitcoin;
import tech.coinbub.daemon.testutils.Dockerized;

@ExtendWith(Dockerized.class)
public class GetBlockHashIT {
    public static final Long HEIGHT = 22L;

    @Test
    public void canGetBlockHash(final Bitcoin bitcoin) {
        final String best = bitcoin.getblockhash(HEIGHT);
        assertThat(best, is(equalTo("29128d296a50754ebc1231a5ceba6b2526c995e4925b48a37159156f54196d73")));
    }
}
