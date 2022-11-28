package kg.peaksoft.peaksoftlmsb6.dto.response;

import kg.peaksoft.peaksoftlmsb6.dto.request.ContentRequest;
import kg.peaksoft.peaksoftlmsb6.entity.Content;
import kg.peaksoft.peaksoftlmsb6.entity.enums.ContentFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContentResponse {
    private Long id;
    private String contentName;
    private ContentFormat contentFormat;
    private String contentValue;

    public ContentResponse(Content content) {
        this.id = content.getId();
        this.contentName = content.getContentName();
        this.contentFormat = content.getContentFormat();
        this.contentValue = content.getContentValue();
    }
}
