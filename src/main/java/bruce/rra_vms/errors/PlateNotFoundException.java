package bruce.rra_vms.errors;

public class PlateNotFoundException extends RuntimeException {
    public PlateNotFoundException(String message) {
        super(message);
    }
}
