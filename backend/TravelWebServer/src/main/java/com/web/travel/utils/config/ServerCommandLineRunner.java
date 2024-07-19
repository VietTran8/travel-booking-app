package com.web.travel.utils.config;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServerCommandLineRunner implements CommandLineRunner {
    private final SocketIOServer socketIOServer;

    @Override
    public void run(String... args) throws Exception {
        try{
            socketIOServer.stop();
        }catch (NullPointerException nullPointerException){
            log.info(nullPointerException.getMessage());
        }finally {
            socketIOServer.start();
        }
    }
}
