package tech.coinbub.daemon.bitcoin;

import tech.coinbub.daemon.Bitcoin;
import tech.coinbub.daemon.testutils.Dockerized;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(Dockerized.class)
public class GetNewAddressIT {
    @Test
    public void canGetAddressForDefaultAccount(final Bitcoin bitcoin) {
        final String address = bitcoin.getnewaddress();
        assertThat(address.length(), is(equalTo(35)));
    }
}
