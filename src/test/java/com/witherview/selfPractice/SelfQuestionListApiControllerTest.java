package com.witherview.selfPractice;

import com.witherview.database.entity.QuestionList;
import com.witherview.database.repository.QuestionListRepository;
import com.witherview.selfPractice.QuestionList.SelfQuestionListDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SelfQuestionListApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private QuestionListRepository questionListRepository;

    @Test
    public void questionList_등록() throws Exception {
        String title = "개발자 스터디 모집해요";
        String enterprise = "네이버";
        String job = "sw개발";
        Integer order = 1;

        SelfQuestionListDTO.SaveDTO requestDto = SelfQuestionListDTO.SaveDTO.builder()
                .title(title)
                .enterprise(enterprise)
                .job(job)
                .order(order)
                .build();

        String url = "http://localhost:" + port + "/self/questionList";

        ResponseEntity<SelfQuestionListDTO.ResponseDTO> responseEntity = restTemplate.postForEntity(url, requestDto, SelfQuestionListDTO.ResponseDTO.class);
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        long id = responseEntity.getBody().getId();
        QuestionList list = questionListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("failed"));
        assertThat(list.getTitle()).isEqualTo(title);
        assertThat(list.getEnterprise()).isEqualTo(enterprise);
        assertThat(list.getJob()).isEqualTo(job);

//        return id;
    }

    @Test
    public void questionList_수정() throws Exception {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        //Long id = questionList_등록();
        long id = 19;

        String updateTitle = "디자이너 스터디 모집";
        String updateEnterprise = "coupang";

        SelfQuestionListDTO.UpdateDTO requestDto =
                SelfQuestionListDTO.UpdateDTO.builder()
                        .title(updateTitle)
                        .enterprise(updateEnterprise)
                        .job("디자이너")
                        .order(2)
                        .build();

        String url = "http://localhost:" + port + "/self/questionList/" + id;
        HttpEntity<SelfQuestionListDTO.UpdateDTO> requestEntity = new HttpEntity<>(requestDto);

        ResponseEntity<SelfQuestionListDTO.ResponseDTO> responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, SelfQuestionListDTO.ResponseDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        QuestionList list = questionListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("failed"));
        assertThat(list.getTitle()).isEqualTo(updateTitle);
        assertThat(list.getEnterprise()).isEqualTo(updateEnterprise);
    }
}
