package Interfaces;

import ENUMS.Ecuotas;
import Excepciones.tarjetaInexistenteEx;

public interface IPago {

    // metodos

    void procesarPago(double monto, Ecuotas cuotas) throws tarjetaInexistenteEx;
}
