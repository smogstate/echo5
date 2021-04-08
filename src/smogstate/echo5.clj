(ns smogstate.echo5
  (:require [mount.core :refer [defstate]]
            [smogstate.config :refer [env]])

  (:import io.netty.bootstrap.ServerBootstrap
           io.netty.channel.nio.NioEventLoopGroup
           io.netty.channel.socket.nio.NioServerSocketChannel
           (io.netty.channel
            ChannelOption ChannelHandler
            ChannelFutureListener
            ChannelInitializer
            EventLoopGroup)
           (io.netty.handler.logging
            LogLevel
            LoggingHandler))
  (:gen-class))

(defn init-server-bootstrap
  [group handlers-factory]
  (.. (ServerBootstrap.)
      (group group)
      (channel NioServerSocketChannel)
      (childHandler
       (proxy [ChannelInitializer] []
         (initChannel [channel]
           (let [handlers (handlers-factory)]
             (.. channel
                 (pipeline)
                 (addLast (into-array ChannelHandler handlers)))))))
      (childOption ChannelOption/SO_KEEPALIVE true)
      (childOption ChannelOption/AUTO_READ false)
      (childOption ChannelOption/AUTO_CLOSE false)))

(defn start-server
  [handlers-factory]
  (let [group (NioEventLoopGroup.)
        bootstrap (init-server-bootstrap group handlers-factory)
        channel (.. bootstrap (bind (:port env)) (sync) (channel))
        closed? (atom false)]
    (println "Starting Echo5 server on port" (:port env))
    (-> channel
        .closeFuture
        (.addListener
         (proxy [ChannelFutureListener] []
           (operationComplete [f]
             (.shutdownGracefully group)))))
    (reify
      java.io.Closeable
      (close [_]
        (when (compare-and-set! closed? false true)
          (println "Stopping Echo5 server...")
          (-> channel .close .sync)
          (-> group .shutdownGracefully .sync)))
      Object
      (toString [_]
        (format "Echo5 Server[channel:%s]" channel)))))

(defstate server
  :start (start-server (fn [] [(LoggingHandler. "proxy" LogLevel/INFO)]))
  :stop (-> server .close))

(defn -main
  [& args]
  (mount.core/start))

(comment (mount.core/stop) )
