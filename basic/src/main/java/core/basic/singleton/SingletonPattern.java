package core.basic.singleton;

public class SingletonPattern {

    private static final SingletonPattern instance = new SingletonPattern();

    private SingletonPattern() {
    }

    public static SingletonPattern getInstance() {
        return instance;
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }

}
