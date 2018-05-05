package tech.coinbub.daemon.bitcoin;

import tech.coinbub.daemon.Bitcoin;
import tech.coinbub.daemon.testutils.Dockerized;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ExtendWith(Dockerized.class)
public class GetBestBlockHashIT {
    public void canGetBestBlockHash(final Bitcoin bitcoin) {
        final String best = bitcoin.getbestblockhash();
        assertThat(best, is(equalTo("3cf12c2be37b180d6c955647cc8b468f814f1c5941dda2283bd9d903882fb6aa")));
    }
}
