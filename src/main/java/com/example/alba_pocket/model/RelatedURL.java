package com.example.alba_pocket.model;

import com.example.alba_pocket.errorcode.CommonStatusCode;
import com.example.alba_pocket.exception.RestApiException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
public class RelatedURL {
    private static final int MAX_LENGTH = 255;

    @Column(nullable = false,length = MAX_LENGTH)
    private String url;

    public RelatedURL(String url) {
        if (isNotValidRelatedURL(url)) {
            throw new RestApiException(CommonStatusCode.NOT_VALIDURL);
        }
        this.url = url;
    }

    private boolean isNotValidRelatedURL(String url) {
        return Objects.isNull(url) || url.length() > MAX_LENGTH || url.isEmpty();

    }

}
