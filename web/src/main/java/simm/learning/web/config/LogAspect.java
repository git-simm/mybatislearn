package simm.learning.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 系统日志，切面处理类
 *
 * @author
 * @email
 * @date
 */
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* simm.learning.web.controllers..*(..))")//指向自定义注解路径
    public void logPointCut() {}

    /**
     * 切面记录系统日志
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")//
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    //保存日志
    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        System.out.println(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String params = objectMapper.writeValueAsString(args[0]);
            System.out.print(params);
        }catch (Exception e){

        }
        //RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        //ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        //HttpServletRequest request = sra.getRequest();
    }
}