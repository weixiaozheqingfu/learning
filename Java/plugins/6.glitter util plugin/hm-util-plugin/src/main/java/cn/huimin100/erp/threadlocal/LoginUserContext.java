package cn.huimin100.erp.threadlocal;

import cn.huimin100.erp.service.ILoginUserService;
import cn.huimin100.erp.util.ApplicationContextHolder;
import cn.huimin100.erp.dto.LoginUserDTO;

public class LoginUserContext {

    private static final ThreadLocal<LoginUserDTO> threadLocal = new ThreadLocal<LoginUserDTO>() {
        // 如果LoginUserInfoDTO.get方法获取到的值为null,则会触发该方法执行,看get方法源码就知道了
        @Override
        protected LoginUserDTO initialValue() {
            ILoginUserService loginUserService = ApplicationContextHolder.getBean(ILoginUserService.class);
            if (null == loginUserService) {
                throw new RuntimeException("not fond implementation class for interface com.glitter.dto.LoginUserInfoDTO.ILoginUserService");
            }
            return loginUserService.getLoginUserInfo();
        }
    };

    public static LoginUserDTO get() {
        return threadLocal.get();
    }

    public static void set(LoginUserDTO loginMember) {
        threadLocal.set(loginMember);
    }

    public static void remove() {
        threadLocal.remove();
    }

}
