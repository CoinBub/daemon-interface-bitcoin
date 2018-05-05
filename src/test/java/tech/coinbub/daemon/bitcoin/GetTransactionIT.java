package tech.coinbub.daemon.bitcoin;

import java.math.BigDecimal;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tech.coinbub.daemon.Bitcoin;
import static tech.coinbub.daemon.testutils.BeanMatcher.hasOnly;
import static tech.coinbub.daemon.testutils.BeanPropertyMatcher.property;
import tech.coinbub.daemon.testutils.Dockerized;

@ExtendWith(Dockerized.class)
public class GetTransactionIT {
    @Test
    public void canGetTransaction(final Bitcoin bitcoin) {
        final Transaction tx = bitcoin.gettransaction("2553da6a3f4130e040c2dddfd9d14b1e394309c393d7f6a7cee8c5005fbb3f87");
        assertThat(tx, hasOnly(
                property("txid", is(equalTo("2553da6a3f4130e040c2dddfd9d14b1e394309c393d7f6a7cee8c5005fbb3f87"))),
                property("hex", is(not(nullValue()))),
                property("amount", is(equalTo(new BigDecimal("0.0")))),
                property("confirmations", is(equalTo(1L))),
                property("generated", is(true)),
                property("blockhash", is(equalTo("3cf12c2be37b180d6c955647cc8b468f814f1c5941dda2283bd9d903882fb6aa"))),
                property("blockindex", is(equalTo(0L))),
                property("blocktime", is(equalTo(1525217577L))),
                property("walletconflicts", hasSize(0)),
                property("time", is(equalTo(1525217560L))),
                property("timereceived", is(equalTo(1525217560L))),
                property("bip125_replaceable", is(equalTo(false))),
                property("details", hasSize(1))
        ));

        final TransactionDetail detail = tx.details.get(0);
        assertThat(detail, hasOnly(
                property("address", is(equalTo("mwh7N9LvZEGsHVWsyfkSxrPepMMSFs4N24"))),
                property("category", is(equalTo(TransactionDetail.Category.immature))),
                property("amount", is(equalTo(new BigDecimal("50.0")))),
                property("vout", is(equalTo(0L)))
        ));
    }
}
