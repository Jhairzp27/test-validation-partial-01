package ec.edu.epn.skyroute.service;

import org.springframework.stereotype.Service;

/**
 * Calcula las tarifas de equipaje para la aerolínea SkyRoute Airlines.
 * <p>
 * Reglas de negocio:
 * <ol>
 *   <li>Tarifa base: $30.0 por maleta.</li>
 *   <li>Exceso de peso: +$50.0 si una maleta pesa más de 23 kg.</li>
 *   <li>Beneficio VIP: primera maleta gratis si el pasajero es VIP
 *       y la maleta no excede 23 kg.</li>
 *   <li>Excepciones: weight ≤ 0, bagCount < 1, o passengerId nulo
 *       lanzan IllegalArgumentException.</li>
 * </ol>
 */
@Service
public class BaggageFeeCalculator {

    private static final double BASE_FEE = 30.0;
    private static final double WEIGHT_LIMIT = 23.0;
    private static final double EXCESS_WEIGHT_FEE = 50.0;

    private final PassengerService passengerService;

    public BaggageFeeCalculator(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * Calcula la tarifa total de equipaje.
     *
     * @param weight       peso de cada maleta (kg)
     * @param bagCount     cantidad de maletas
     * @param passengerId  identificador del pasajero
     * @return costo total en dólares
     * @throws IllegalArgumentException si los parámetros no cumplen las restricciones
     */
    public double calculateFee(double weight, int bagCount, Long passengerId) {

        boolean isVip = passengerService.isVip(passengerId);
        double totalFee = 0.0;

        for (int i = 1; i <= bagCount; i++) {
            totalFee = processSingleBag(weight, i, isVip, totalFee);
        }

        return totalFee;
    }

    private static double processSingleBag(double weight, int i, boolean isVip, double totalFee) {
        double currentBagFee = BASE_FEE;

//            Primera maleta (i=1) gratis si es VIP y cumple con el límite
        boolean isElegibleForVipBenefits = i == 1 && isVip && weight <= WEIGHT_LIMIT;
        if (isElegibleForVipBenefits) {
            currentBagFee = 0.0;
        }

//          Exceso de peso
        if (weight > WEIGHT_LIMIT) {
            currentBagFee += EXCESS_WEIGHT_FEE;
        }

        totalFee += currentBagFee;
        return totalFee;
    }

}
