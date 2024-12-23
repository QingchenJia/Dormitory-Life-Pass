package dormitorylifepass.controller;

import dormitorylifepass.service.CheckInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkIn")
@Slf4j
public class CheckInController {
    @Autowired
    private CheckInService checkInService;
}
