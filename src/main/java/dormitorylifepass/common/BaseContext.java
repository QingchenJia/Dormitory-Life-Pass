package dormitorylifepass.common;

public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程关联的用户ID
     * 这个方法用于在给定的线程中存储用户ID信息，可以用于跨方法调用的身份识别
     *
     * @param id 用户ID，用于标识一个用户
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前线程的唯一标识符
     * <p>
     * 此方法用于获取存储在ThreadLocal中的当前线程的唯一ID，ThreadLocal为每个线程都提供了独立的变量副本，
     * 通过这种方式实现线程安全，每个线程都可以独立地修改自己的副本，而不会影响其他线程
     *
     * @return 当前线程的唯一标识符，可能为null如果尚未为当前线程设置ID
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
