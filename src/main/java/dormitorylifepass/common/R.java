package dormitorylifepass.common;

import lombok.Data;

@Data
public class R<T> {
    private Integer code;

    private String msg;

    private T data;

    /**
     * 创建一个成功的响应对象，包含指定的数据
     * 此方法用于当操作成功且需要返回数据时，封装数据到响应对象中
     *
     * @param data 成功操作后需要返回的数据，可以是任意类型
     * @return 返回一个包含指定数据的成功响应对象
     */
    public static <T> R<T> success(T data) {
        // 创建一个新的响应对象
        R<T> result = new R<>();
        // 设置响应代码为1，表示成功
        result.code = 1;
        // 设置响应数据为传入的data参数
        result.data = data;
        // 返回构建好的响应对象
        return result;
    }

    /**
     * 创建一个表示错误响应的方法
     * 该方法用于生成一个带有错误信息的响应对象，便于统一错误处理和消息格式
     *
     * @param <T> 响应对象中数据的类型，由于错误响应通常不包含具体数据，此处泛型主要用于兼容其他响应类型
     * @param msg 错误消息，用于向调用者说明错误原因
     * @return 返回一个包含错误信息的响应对象
     */
    public static <T> R<T> error(String msg) {
        // 创建一个新的响应对象
        R<T> result = new R<>();
        // 设置响应代码为0，表示成功
        result.code = 0;
        // 设置响应消息为传入的msg参数
        result.msg = msg;
        // 返回构建好的响应对象
        return result;
    }
}
