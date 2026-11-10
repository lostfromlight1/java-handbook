/** Module 09 Ch.03 — prefer composition for reuse of behavior. */
public class CompositionVsInheritanceDemo {
    public static void main(String[] args) {
        Engine engine = new Engine(180);
        Car car = new Car("Sedan", engine);
        System.out.println(car.describe());
        System.out.println("start=" + car.start());

        // Inheritance alternative would be Car extends Engine — wrong is-a
        Logger logger = new ConsoleLogger();
        AuditedService svc = new AuditedService(logger);
        System.out.println(svc.execute("save"));
    }
}

class Engine {
    private final int hp;

    Engine(int hp) {
        this.hp = hp;
    }

    boolean start() {
        return true;
    }

    String describe() {
        return hp + "hp";
    }
}

class Car {
    private final String model;
    private final Engine engine;

    Car(String model, Engine engine) {
        this.model = model;
        this.engine = engine;
    }

    boolean start() {
        return engine.start();
    }

    String describe() {
        return model + " / " + engine.describe();
    }
}

interface Logger {
    void log(String msg);
}

class ConsoleLogger implements Logger {
    @Override
    public void log(String msg) {
        System.out.println("log:" + msg);
    }
}

class AuditedService {
    private final Logger logger;

    AuditedService(Logger logger) {
        this.logger = logger;
    }

    String execute(String action) {
        logger.log("begin " + action);
        logger.log("end " + action);
        return "done:" + action;
    }
}
