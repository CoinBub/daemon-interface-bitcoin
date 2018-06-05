package tech.coinbub.daemon.bitcoin;

import java.math.BigDecimal;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tech.coinbub.daemon.NormalizedBitcoin;
import tech.coinbub.daemon.normalization.model.Block;
import tech.coinbub.daemon.normalization.model.Transaction;
import tech.coinbub.daemon.normalization.model.TransactionDetail;
import static tech.coinbub.daemon.testutils.BeanMatcher.hasOnly;
import static tech.coinbub.daemon.testutils.BeanPropertyMatcher.property;
import tech.coinbub.daemon.testutils.Dockerized;

@ExtendWith(Dockerized.class)
public class NormalizedBitcoinIT {
    @Test
    public void testGetblockhash(final NormalizedBitcoin normalized) {
        assertThat(normalized.getblockhash(22L), is(equalTo("29128d296a50754ebc1231a5ceba6b2526c995e4925b48a37159156f54196d73")));
    }

    @Test
    public void testGetblock(final NormalizedBitcoin normalized) {
        final Block block = normalized.getblock("3cf12c2be37b180d6c955647cc8b468f814f1c5941dda2283bd9d903882fb6aa");
        assertThat(block, hasOnly(
                property("hash", is(equalTo("3cf12c2be37b180d6c955647cc8b468f814f1c5941dda2283bd9d903882fb6aa"))),
                property("confirmations", is(equalTo(1L))),
                property("size", is(equalTo(253L))),
                property("height", is(equalTo(102L))),
                property("time", is(equalTo(1525217577L))),
                property("previousblockhash", is(equalTo("3382d4f9f9f2c4268c7ad8db2330584dbcfc6c6f31e03894b3766e43ed67e020"))),
                property("tx", hasItem("2553da6a3f4130e040c2dddfd9d14b1e394309c393d7f6a7cee8c5005fbb3f87"))
        ));
    }

    @Test
    public void testGettransaction(final NormalizedBitcoin normalized) {
        final Transaction tx = normalized.gettransaction("2553da6a3f4130e040c2dddfd9d14b1e394309c393d7f6a7cee8c5005fbb3f87");
        assertThat(tx, hasOnly(
                property("id", is(equalTo("2553da6a3f4130e040c2dddfd9d14b1e394309c393d7f6a7cee8c5005fbb3f87"))),
                property("amount", is(equalTo(new BigDecimal("0.0")))),
                property("confirmations", is(equalTo(1L))),
                property("blockhash", is(equalTo("3cf12c2be37b180d6c955647cc8b468f814f1c5941dda2283bd9d903882fb6aa"))),
                property("time", is(equalTo(1525217560L))),
                property("details", hasSize(1))
        ));

        final TransactionDetail detail = tx.details.get(0);
        assertThat(detail, hasOnly(
                property("address", is(equalTo("mwh7N9LvZEGsHVWsyfkSxrPepMMSFs4N24"))),
                property("amount", is(equalTo(new BigDecimal("50.0"))))
        ));
    }

    @Test
    public void testGetnewaddress(final NormalizedBitcoin normalized) {
        assertThat(normalized.getnewaddress().length(), is(equalTo(35)));
    }

    @Test
    public void testSendToAddressNoComments(final NormalizedBitcoin normalized) {
        final String txid = normalized.sendtoaddress(SendToAddressIT.NEW_ADDRESS, BigDecimal.ONE);
        final Transaction tx = normalized.gettransaction(txid);
        assertThat(tx, hasOnly(
                property("id", is(not(nullValue()))),
                property("amount", is(equalTo(new BigDecimal("-1.0")))),
                property("fee", is(equalTo(new BigDecimal("-0.0000332")))),
                property("time", is(not(nullValue()))),
                property("confirmations", is(equalTo(0L))),
                property("details", hasSize(1))
        ));

        
        final TransactionDetail detail = tx.details.get(0);
        assertThat(detail, hasOnly(
                property("address", is(equalTo(SendToAddressIT.NEW_ADDRESS))),
                property("amount", is(equalTo(new BigDecimal("-1.0")))),
                property("fee", is(equalTo(new BigDecimal("-0.0000332"))))
        ));
    }

    @Test
    public void testSendToAddressSourceComment(final NormalizedBitcoin normalized) {
        final String txid = normalized.sendtoaddress(SendToAddressIT.NEW_ADDRESS, BigDecimal.ONE, "test transaction!");
        final Transaction tx = normalized.gettransaction(txid);
        assertThat(tx, hasOnly(
                property("id", is(not(nullValue()))),
                property("amount", is(equalTo(new BigDecimal("-1.0")))),
                property("fee", is(equalTo(new BigDecimal("-0.0000332")))),
                property("time", is(not(nullValue()))),
                property("confirmations", is(equalTo(0L))),
                property("details", hasSize(1)),
                property("comment_from", is(equalTo("test transaction!")))
        ));
        
        final TransactionDetail detail = tx.details.get(0);
        assertThat(detail, hasOnly(
                property("address", is(equalTo(SendToAddressIT.NEW_ADDRESS))),
                property("amount", is(equalTo(new BigDecimal("-1.0")))),
                property("fee", is(equalTo(new BigDecimal("-0.0000332"))))
        ));
    }

    @Test
    public void testSendToAddressDestComment(final NormalizedBitcoin normalized) {
        final String txid = normalized.sendtoaddress(SendToAddressIT.NEW_ADDRESS, BigDecimal.ONE, "test transaction!", "receiving test!");
        final Transaction tx = normalized.gettransaction(txid);
        assertThat(tx, hasOnly(
                property("id", is(not(nullValue()))),
                property("amount", is(equalTo(new BigDecimal("-1.0")))),
                property("fee", is(equalTo(new BigDecimal("-0.0000332")))),
                property("time", is(not(nullValue()))),
                property("confirmations", is(equalTo(0L))),
                property("details", hasSize(1)),
                property("comment_from", is(equalTo("test transaction!"))),
                property("comment_to", is(equalTo("receiving test!")))
        ));
        
        final TransactionDetail detail = tx.details.get(0);
        assertThat(detail, hasOnly(
                property("address", is(equalTo(SendToAddressIT.NEW_ADDRESS))),
                property("amount", is(equalTo(new BigDecimal("-1.0")))),
                property("fee", is(equalTo(new BigDecimal("-0.0000332"))))
        ));
    }

}
