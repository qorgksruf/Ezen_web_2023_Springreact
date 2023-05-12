package ezenweb.web.domain.File;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDto {

    private String uuidFile;         //식별이 포함된 파일명
    private String originalFilename; //실제 순수 파일명
    private String sizekb;           //용량 kb


}
