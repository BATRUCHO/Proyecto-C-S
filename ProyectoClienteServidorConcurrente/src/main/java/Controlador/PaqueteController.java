package Controlador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Dominio.Paquete;

public class PaqueteController {

    private final List<Paquete> paquetes = new ArrayList<>();

    public void addPaquete(Paquete p) {
        if (p != null) {
            paquetes.add(p);
        }
    }

    public List<Paquete> getPaquetes() {
        return Collections.unmodifiableList(paquetes);
    }

    public void clearPaquetes() {
        paquetes.clear();
    }
}