package bruce.rra_vms.utils;

import java.util.Random;

public class RandomGenerator {
    public String generateRandomPlateNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder("RA");

        // Add a random uppercase letter
        number.append((char) (random.nextInt(26) + 'A'));

        // Add 3 random digits
        for (int i = 0; i < 3; i++) {
            number.append(random.nextInt(10));
        }

        // Add a final random uppercase letter
        number.append((char) (random.nextInt(26) + 'A'));

        return number.toString();
    }

    public String generateChassisNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder("AN");

        // Add 5 random digits
        for (int i = 0; i < 5; i++) {
            number.append(random.nextInt(10));
        }

        return number.toString();
    }
}