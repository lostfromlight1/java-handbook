/** Module 08 Ch.04 — API design with interfaces: depend on abstractions. */
public class ApiInterfacesDemo {
    public static void main(String[] args) {
        UserRepository repo = new InMemoryUserRepository();
        UserService service = new UserService(repo);
        System.out.println(service.register("ada"));
        System.out.println(service.register("ada"));
        System.out.println("find=" + service.find("ada"));
    }
}

interface UserRepository {
    boolean exists(String username);
    void save(String username);
    String find(String username);
}

class InMemoryUserRepository implements UserRepository {
    private String stored;

    @Override
    public boolean exists(String username) {
        return stored != null && stored.equals(username);
    }

    @Override
    public void save(String username) {
        stored = username;
    }

    @Override
    public String find(String username) {
        return exists(username) ? stored : null;
    }
}

class UserService {
    private final UserRepository repo;

    UserService(UserRepository repo) {
        this.repo = repo;
    }

    String register(String username) {
        if (repo.exists(username)) {
            return "exists:" + username;
        }
        repo.save(username);
        return "created:" + username;
    }

    String find(String username) {
        String found = repo.find(username);
        return found == null ? "missing" : found;
    }
}
