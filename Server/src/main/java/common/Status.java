package common;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

public class Status
{
    public static class CodeDescriptor
    {
        public CodeDescriptor(int code, String description)
        {
            this.code = code;
            this.description = description;
        }

        private int code;
        private String description;

        public int code()
        {
            return code;
        }
    }

    public static class Code
    {
        public static final CodeDescriptor OK = new CodeDescriptor(0, "成功");
        public static final CodeDescriptor USER_NOT_EXIST = new CodeDescriptor(1, "账户不存在");
        public static final CodeDescriptor INCORRECT_USER_NAME_OR_PASSWORD = new CodeDescriptor(2, "用户名或密码错误");
        public static final CodeDescriptor TOKEN_FAILURE = new CodeDescriptor(3, "身份过期,请重新登录");
        public static final CodeDescriptor USER_NAME_ALREADY_EXIST = new CodeDescriptor(4, "用户名已存在");
        public static final CodeDescriptor USER_NAME_NOT_VALID = new CodeDescriptor(5, "用户名不合法");
        public static final CodeDescriptor INVALID_TOKEN = new CodeDescriptor(6, "无效的身份信息");

        public static final CodeDescriptor UNKNOWN_ERROR = new CodeDescriptor(500, "未知错误");

    }

    //状态码
    @Getter
    @Setter
    private int code = 0;

    //消息
    @Getter
    @Setter
    private String message = null;

    //数据对象
    @Getter
    @Setter
    private Object data = null;


    public Status()
    {
    }

    public Status(int code)
    {
        this.code = code;
    }

    public Status(CodeDescriptor codeDescriptor, String message)
    {
        this.code = codeDescriptor.code;
        this.message = message;
    }

    public Status(CodeDescriptor codeDescriptor)
    {
        this.code = codeDescriptor.code;
        this.message = codeDescriptor.description;
    }

    public Status(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public Status(int code, String message, Object data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
