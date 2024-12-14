package dormitorylifepass;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DormitoryLifePassApplication {

    public static void main(String[] args) {
        SpringApplication.run(DormitoryLifePassApplication.class, args);
        log.info("""
                                  
                -------------------------------------------------------
                \tApplication:\tDormitoryLifePass Starting...
                \tApi-doc:\thttp://localhost:8080/doc.html
                -------------------------------------------------------
                """);
    }

}
