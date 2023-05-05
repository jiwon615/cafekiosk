package com.study.cafekiosk.spring.api.service.mail;

import com.study.cafekiosk.spring.client.mail.MailSendClient;
import com.study.cafekiosk.spring.domain.history.MailSendHistory;
import com.study.cafekiosk.spring.domain.history.MailSendHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// 테스트 수행시 mockito 사용할 테스트가 있다는 것을 인지하도록 함
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    /**
     * @Spy : 일부만 stubbing 하고싶을 때 사용 (객체에서 일부는 진짜 객체를 쓰고 싶고 특정 부분만 가짜 객체 쓰고자 할 때 spy 사용)
     * @Mock: 가짜 객체를 사용하여, 해당 객체의 전체를 모두 가짜 객체로 사용하게 됨
     */
    @Mock
//    @Spy
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // given

        /*
         * MailSendClient와 MailSendHistoryRepository 를 Mock으로 만들어주고 MailService에 주입해준다.
         */
//        MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
//        MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);

//        MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

        /**
         * stubbing
         *
         * Mochito.when().thenReturn()과 BDDMockito.given().willReturn()
         * - 둘 다 동일하게 동작
         * - BDDMockito가 given() 이름을 가져 더 역할에 자연스럽고, 내부적으로 Mockito를 상속받음
         *
         */
        Mockito.when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);

        BDDMockito.given(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
                .willReturn(true);
        /*
         * @Spy 사용 하게 되면 stubbing 시 사용한 위와 같은 when().thenReturn() 형식이 아닌 아래처럼 사용할 것
         * (sendMail() 부분만 가짜 객체를 사용하게 됨)
         */
//        doReturn(true)
//                .when(mailSendClient)
//                .sendMail(anyString(), anyString(), anyString(), anyString());

        // when
        boolean result = mailService.sendMail("", "", "", "");

        // then
        assertThat(result).isTrue();
        // save라는 메소드가 1번 호출되어쓴지 검증
        Mockito.verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));

    }
}