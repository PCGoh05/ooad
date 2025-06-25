import java.util.ArrayList;
import java.util.List;

public class RegistrationManager {
    private static RegistrationManager instance;
    private List<Registration> registrations;

    private RegistrationManager() {
        registrations = new ArrayList<>();
    }

    public static RegistrationManager getInstance() {
        if (instance == null) {
            instance = new RegistrationManager();
        }
        return instance;
    }

    public void addRegistration(Registration reg) {
        registrations.add(reg);
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public List<Registration> getRegistrationsByUser(User user) {
        List<Registration> result = new ArrayList<>();
        for (Registration r : registrations) {
            if (r.getUser().equals(user)) {
                result.add(r);
            }
        }
        return result;
    }
}
