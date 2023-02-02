package com.example.alba_pocket.model;

import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.errorcode.StatusCode;
import com.example.alba_pocket.exception.RestApiException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
public class NotificationContent {

    private static final int Max_LENGTH = 80;

    @Column(nullable = false,length = Max_LENGTH)
    private String content;

    public NotificationContent(String content){
        if(isNotValidNotificationContent(content)){
            throw new RestApiException(CommonStatusCode.NOT_VALIDCONTENT);
        }
        this.content = content;
    }

    private boolean isNotValidNotificationContent(String content) {
        return Objects.isNull(content) || content.length() > Max_LENGTH
                || content.isEmpty();

    }
}
