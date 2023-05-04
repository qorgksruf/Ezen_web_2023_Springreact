package ezenweb.web.controller;

import ezenweb.web.domain.File.FileDto;
import ezenweb.web.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private FileService fileService;

    @PostMapping("fileupload") //chat관련 첨부파일 업로드
    public FileDto fileUpload(@RequestParam("attachFile") MultipartFile multipartFile){

        FileDto result= fileService.fileUpload(multipartFile);

        return result;

    }

/*    @GetMapping("filedownload") //chat 관련 첨부파일 다운로드
    public String filedownload(@RequestParam("filepath") String filepath){
        return null;
    }*/
}
