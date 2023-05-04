package ezenweb.web.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration // 컴포넌트 등록
@EnableWebSocket // * WS 프로토콜의 URL 매핑
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired // 컴포넌트에 등록한 클래스이므로 @Autowired 가능
    private ChattingHandler chattingHandler;

    @Override // 서버소켓으로 사용되고 있는 클래스들 등록
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler( chattingHandler , "/chat").setAllowedOrigins("*");
        // registry.addHandler( 서버소켓 객체 , "서버소켓 path" ) : 서버소켓 등록 함수
            // .setAllowedOrigins("접속허용 도메인") : 해당 서버소켓으로부터 요청할수 있는 URL/도메인
            // .setAllowedOrigins("*") : 모든 도메인 요청 가능
    }

}
