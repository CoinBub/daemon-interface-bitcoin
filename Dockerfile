# Dockerfile for Bitcoin coin daemon

#
# Daemon executable builder
#
FROM ubuntu:16.04 as builder

RUN apt-get -y update \
 && apt-get -y install software-properties-common

RUN add-apt-repository ppa:bitcoin/bitcoin \
 && apt-get -y update \
 && apt-get -y install \
        autoconf \
        bsdmainutils \
        build-essential \
        git \
        iproute2 \
        libboost-all-dev \
        libdb4.8-dev \
        libdb4.8++-dev \
        libevent-dev \
        libssl-dev \
        libminiupnpc-dev \
        libqrencode-dev \
        libtool \
        pkgconf \
        pkg-config \
        wget


RUN git clone https://github.com/bitcoin/bitcoin.git /tmp/bitcoin \
 && cd /tmp/bitcoin \
 && ./autogen.sh \
 && ./configure \
 && make STATIC=all -s -j5


#
# Final Docker image
#
FROM ubuntu:16.04
EXPOSE 3333 13754 13755

COPY --from=builder /tmp/bitcoin/src/bitcoind /usr/local/bin/bitcoind
COPY --from=builder /tmp/bitcoin/src/bitcoin-cli /usr/local/bin/bitcoin-cli
COPY .docker /

RUN apt-get -y update \
 && apt-get -y install software-properties-common

RUN add-apt-repository ppa:bitcoin/bitcoin \
 && apt-get -y update \
 && apt-get -y install \
        gettext \
        iproute2 \
        libboost-filesystem-dev \
        libboost-program-options-dev \
        libboost-system-dev \
        libboost-thread-dev \
        libdb4.8-dev \
        libdb4.8++-dev \
        libevent-dev \
        libssl-dev \
        libminiupnpc-dev \
        libqrencode-dev \
        netcat \
        pwgen
RUN groupadd --gid 1000 bitcoin \
 && useradd --uid 1000 --gid bitcoin --shell /bin/false --create-home bitcoin \
 && chmod +x /usr/local/bin/notifier \
 && chmod +x /usr/local/bin/entrypoint \
 && mkdir /home/bitcoin/.bitcoin \
 && chown -R bitcoin.bitcoin /home/bitcoin/.bitcoin/

USER bitcoin
WORKDIR /home/bitcoin

ENTRYPOINT [ "/usr/local/bin/entrypoint" ]
CMD [ "bitcoind", "-printtoconsole" ]