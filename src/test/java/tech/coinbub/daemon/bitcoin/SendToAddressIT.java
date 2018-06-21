package tech.coinbub.daemon.bitcoin;

import com.googlecode.jsonrpc4j.JsonRpcClientException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Observable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tech.coinbub.daemon.Bitcoin;
import tech.coinbub.daemon.NormalizedBitcoin;
import tech.coinbub.daemon.NotificationListener;
import static tech.coinbub.daemon.testutils.BeanMatcher.has;
import static tech.coinbub.daemon.testutils.BeanPropertyMatcher.property;
import tech.coinbub.daemon.testutils.Dockerized;

@ExtendWith(Dockerized.class)
public class SendToAddressIT {
    public static final int LISTENER_PORT = 20010;
    public static final String NEW_ADDRESS = "2N33YgELRYjWzn1whQ614uDDNGfNJ2X5A7d";

    private NotificationListener listener = null;
    private Object result = null;
    private final CountDownLatch latch = new CountDownLatch(1);

    @AfterEach
    public void teardown() throws IOException {
        if (listener != null) {
            listener.stop();
        }
    }

    @Test
    public void throwsErrorOnInvalidAddress(final Bitcoin bitcoin) {
        final JsonRpcClientException ex = Assertions.assertThrows(JsonRpcClientException.class, () -> {
            bitcoin.sendtoaddress("abc", BigDecimal.ONE);
        });
        assertThat(ex.getMessage(), is(equalTo("Invalid address")));
    }

    @Test
    public void supportsNoComments(final Bitcoin bitcoin, final NormalizedBitcoin normalized) throws IOException, InterruptedException {
        setup(normalized);
        final String txid = bitcoin.sendtoaddress(NEW_ADDRESS, BigDecimal.ONE);
        final Transaction tx = bitcoin.gettransaction(txid);
        assertThat(tx.details, hasSize(1));
        assertThat(tx.details.get(0).amount, is(equalTo(new BigDecimal("-1.0"))));
        latch.await(500, TimeUnit.MILLISECONDS);
        assertThat(result, is(not(nullValue())));
        assertThat(((tech.coinbub.daemon.normalization.model.Transaction) result).id.length(), is(equalTo(64)));
    }

    @Test
    public void supportsSourceComment(final Bitcoin bitcoin, final NormalizedBitcoin normalized) throws IOException, InterruptedException {
        setup(normalized);
        final String txid = bitcoin.sendtoaddress(NEW_ADDRESS, BigDecimal.ONE, "test transaction!");
        final Transaction tx = bitcoin.gettransaction(txid);
        assertThat(tx, has(
                property("details", hasSize(1)),
                property("comment", is(equalTo("test transaction!")))
        ));
        
        final TransactionDetail detail = tx.details.get(0);
        assertThat(detail, has(
                property("address", is(equalTo(NEW_ADDRESS))),
                property("category", is(equalTo(TransactionDetail.Category.send))),
                property("amount", is(equalTo(new BigDecimal("-1.0"))))
        ));

        latch.await(500, TimeUnit.MILLISECONDS);
        assertThat(result, is(not(nullValue())));
        assertThat(((tech.coinbub.daemon.normalization.model.Transaction) result).id.length(), is(equalTo(64)));
    }

    @Test
    public void supportsDestinationComment(final Bitcoin bitcoin, final NormalizedBitcoin normalized) throws IOException, InterruptedException {
        setup(normalized);
        final String txid = bitcoin.sendtoaddress(NEW_ADDRESS, BigDecimal.ONE, "test transaction!", "receiving test!");
        final Transaction tx = bitcoin.gettransaction(txid);
        assertThat(tx, has(
                property("details", hasSize(1)),
                property("comment", is(equalTo("test transaction!"))),
                property("to", is(equalTo("receiving test!")))
        ));
        
        final TransactionDetail detail = tx.details.get(0);
        assertThat(detail, has(
                property("address", is(equalTo(NEW_ADDRESS))),
                property("category", is(equalTo(TransactionDetail.Category.send))),
                property("amount", is(equalTo(new BigDecimal("-1.0"))))
        ));

        latch.await(500, TimeUnit.MILLISECONDS);
        assertThat(result, is(not(nullValue())));
        assertThat(((tech.coinbub.daemon.normalization.model.Transaction) result).id.length(), is(equalTo(64)));
    }

    private void setup(final NormalizedBitcoin normalized) throws IOException {
        result = null;

        listener = new NotificationListener(LISTENER_PORT);
        listener.setTransformer(new NotificationListener.TransactionTransformer(normalized));
        listener.addObserver((Observable o, Object o1) -> {
            result = o1;
            latch.countDown();
        });
    }
}
