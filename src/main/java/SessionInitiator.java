import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class SessionInitiator {
    private final String host;
    private final int port;
    private final String senderCompId;
    private final String targetCompId;
    private EventLoopGroup group;
    private ScheduledExecutorService scheduler;
    private SessionInitiatorHandler sessionInitiatorHandler;
    private Consumer<String> stringConsumer;

    public SessionInitiator(String host, int port, String senderCompId, String targetCompId, Consumer<String> stringConsumer) {
        this.host = host;
        this.port = port;
        this.senderCompId = senderCompId;
        this.targetCompId = targetCompId;
        this.stringConsumer = stringConsumer;
    }

    public ChannelFuture start() throws Exception{

        group = new NioEventLoopGroup();
        try{
            sessionInitiatorHandler = new SessionInitiatorHandler(SessionInitiator.this, senderCompId, targetCompId, this.stringConsumer);
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("clientHandler", sessionInitiatorHandler);
                        }
                    });
            ChannelFuture f = b.connect().sync();
            return f;
        }finally{

        }
    }

    public void stop() throws IOException{
        if(scheduler != null){
            scheduler.shutdown();
        }

        this.sessionInitiatorHandler.getSessionData().getOutDataWriter().close();
        this.sessionInitiatorHandler.getSessionData().getInDataWriter().close();
        group.shutdownGracefully();

    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getSenderCompId() {
        return senderCompId;
    }

    public String getTargetCompId() {
        return targetCompId;
    }

    public EventLoopGroup getGroup() {
        return group;
    }

    public void setGroup(EventLoopGroup group) {
        this.group = group;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public void setScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public SessionInitiatorHandler getSessionInitiatorHandler() {
        return sessionInitiatorHandler;
    }

    public void setSessionInitiatorHandler(SessionInitiatorHandler sessionInitiatorHandler) {
        this.sessionInitiatorHandler = sessionInitiatorHandler;
    }

    public Consumer<String> getStringConsumer() {
        return stringConsumer;
    }

    public void setStringConsumer(Consumer<String> stringConsumer) {
        this.stringConsumer = stringConsumer;
    }
}
