package dormitorylifepass.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理所有未捕获的异常
     *
     * @param e 异常对象
     * @return 响应实体，包含错误信息和状态码
     */
    @ExceptionHandler(Exception.class)
    public R<String> handleException(Exception e) {
        // 记录异常信息，用于调试和追踪错误
        log.warn("全局异常捕获：" + e.getMessage());
        // 返回错误响应，告知客户端发生错误
        return R.error("操作失败");
    }
}
