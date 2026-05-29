package ec.edu.epn.skyroute.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BaggageFeeCalculatorTest {
    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private BaggageFeeCalculator calculator;

    @Test
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Debería cobrar $30.0 por una maleta estándar para un pasajero regular")
    void deberiaCobrar30_WhenEsUnaMaletaEstandarYPasajeroRegular() {
//        ARRANGE
        Long passengerId = 1L;
        double weight = 20.0;
        int bagCount = 1;

        when(passengerService.isVip(passengerId)).thenReturn(false);

//        ACT
        double result = calculator.calculateFee(weight, bagCount, passengerId);

//        ASSERT
        assertEquals(30.0, result, "El resultado no coincide con el esperado, La tarifa base debe ser de $30");
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Exceso de peso 1 maleta, 25 kg, pasajero regular, Cobra 80")
    void deberiaCobrar80_CuandoHayExcesoDePesoYPasajeroRegular() {
//        ARRANGE
        Long passengerId = 1L;
        double weight = 25.0;
        int bagCount = 1;

        when(passengerService.isVip(passengerId)).thenReturn(false);

//        ACT
        double result = calculator.calculateFee(weight, bagCount, passengerId);

//        ASSERT
//        30 (base) + 50 (exceso) = 80
        assertEquals(80.0, result, "El costo total con exceso de peso debe ser $80.0");

    }


    @Test
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Beneficio VIP - 1 maleta, 15 kg, pasajero VIP")
    void deberiaCobrar0_CuandoEsPasajeroVIPYMaletaEstandar() {
//        ARRANGE
        Long passengerId = 1L;
        double weight = 25.0;
        int bagCount = 1;

        when(passengerService.isVip(passengerId)).thenReturn(true);

//        ACT
        double result = calculator.calculateFee(weight, bagCount, passengerId);

//        ASSERT
        assertEquals(0.0, result, "El beneficio VIP debe cubrir el costo total de la primera maleta estándar");

    }

    @Test
    @DisplayName("VIP con 2 maletas - Solo la primera maleta es gratis")
    void deberiaCobrar30_CuandoEsVIPConDosMaletasEstandar() {
//        ARRANGE
        Long passengerId = 4L;
        double weight = 10.0;
        int bagCount = 2;
        when(passengerService.isVip(passengerId)).thenReturn(true);

//        ACT
        double result = calculator.calculateFee(weight, bagCount, passengerId);

//        ASSERT
        assertEquals(30.0, result, "El beneficio VIP solo debe aplicar a la primera maleta");
    }



}