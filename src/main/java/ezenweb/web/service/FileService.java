package ezenweb.web.service;

import ezenweb.web.domain.File.FileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    // 첨부파일 저장 할 경로 [1. 배포전 , 2.배포 후 ]
        String path= "C:\\java\\";

    public FileDto fileUpload( MultipartFile multipartFile){
        log.info("File upload: " + multipartFile);
        log.info("File upload: " + multipartFile.getOriginalFilename()); //실제 첨부파일 파일명
        log.info("File upload: " + multipartFile.getName());             //input name
        log.info("File upload: " + multipartFile.getContentType());      //첨부파일 확장자
        log.info("File upload: " + multipartFile.getSize());             //99 995 바이트

        if( !multipartFile.getOriginalFilename().equals("") ){ //첨부파일이 존재하면
            //* 만약에 다른 이미지인데 파일이 동일하면 중복발생 [식별불가]
            String fileName = UUID.randomUUID().toString()+"_"+multipartFile.getOriginalFilename();


            //2.경로 + 파일명 조합해서 file 클래스의 객체 생성 [왜? 파일?? transferTo가 file class 만 받을 수 있어서 그렇게 만들어주는 것임 ]
            File file = new File( path + fileName );
            //3.업로드 //multipartFile.transferTo (저장할 file 클래스의 객체)
            try {
                multipartFile.transferTo(file);
            }catch (Exception e){
                    log.info("file upload fail:"+e);
            }

            return FileDto.builder()
                    .originalFilename(multipartFile.getOriginalFilename())
                    .uuidFile(fileName)
                    .sizekb(multipartFile.getSize()/1024+"kb")
                    .build();

        }

        return null;

    }

    @Autowired
    private HttpServletResponse response; //응답객체

    public void filedownload( String uuidFile){ //spring 다운로드 관한 api 없음
        String pathFile = path + uuidFile; //경로 + uuid 파일명: 실제 파일이 존재하는 위치
        try{
            response.setHeader(
                    "Content-Disposition", //헤더 구역 [브라우저 다운로드 형식]
                    "attachment;filename=" + URLEncoder.encode(uuidFile.split("_")[1], "UTF-8") ); //다운로드시 화면에 표시될 이름
                                            //uuid 는 지운거임~ 그리고 한글꺠지지 않도록 excode함
            //다운로드 스트림 구성
            File file = new File(pathFile); //다운로드 할 파일의 경로에서 파일 객체화

            //스트림 [ 서버가 먼저 다운로드 할 파일의 바이트 읽어오기 =대상 : 클라이언트가 요청한 파일]
            BufferedInputStream fin = new BufferedInputStream( new FileInputStream(file) );
            byte[] bytes = new byte[(int) file.length()]; //파일의 길이[용량]만큼 바이트 배열 선언 즉 용량을 알려줌
            fin.read(bytes); //읽어온 바이트들을 bytes 배열 저장

            //4. 스티림 [서버가 읽어온 바이트를 출력할 스트림] = 대상: response = 현재 서비스 요청한 클라이언트 객체
            BufferedOutputStream fout = new BufferedOutputStream( response.getOutputStream() );
            fout.write( bytes );//입력스트림에서 읽어온 바이트 배열을 내보내기
            fout.flush();       // 스트림 메모리 초기화
            fin.close();        //스트림메모리 초기화
            fout.close();       //스트림 닫기

        }catch (Exception e){
            System.out.println(e);


        }



    }


}
