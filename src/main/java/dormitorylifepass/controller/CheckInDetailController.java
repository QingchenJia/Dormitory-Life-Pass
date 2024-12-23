package dormitorylifepass.controller;

import dormitorylifepass.service.CheckInDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkInDetail")
@Slf4j
public class CheckInDetailController {
    @Autowired
    private CheckInDetailService checkInDetailService;
}
