package monte_carlo;

import java.time.LocalTime;
import data.Data;
import java.util.Random;

public class Persona {
    LocalTime horaLlegada, tiempoLlegadas, horaAtencion, tiempoEspera, tiempoOperacion, horaSalida;
    Operacion operacion;

    public Persona(Persona anteriorPersona) {
        if (anteriorPersona == null)
            setDefaults();
        else
            setValues(anteriorPersona);
        tiempoLlegadas = tiempoLlegadas();
        tiempoEspera = tiempoEspera();
        operacion = operacion();
        tiempoOperacion = tiempoOperacion();
        horaSalida();
    }

    private void setDefaults() {
        horaLlegada = horaLlegada(null, null);
        horaAtencion(null);
    }

    private void setValues(Persona anteriorPersona) {
        horaLlegada = horaLlegada(anteriorPersona.horaLlegada,
                                  anteriorPersona.tiempoLlegadas);
        horaAtencion(anteriorPersona.horaSalida);
    }

    private LocalTime horaLlegada(LocalTime horaAnterior, LocalTime llegadasAnterior) {
        if (horaAnterior == null)
            return LocalTime.of(7, 0);
        else
            return horaAnterior.plusNanos(llegadasAnterior.toNanoOfDay());
    }

    private LocalTime tiempoLlegadas() {
        Data info = Data.getInstance();
        return new Estadistica(info.media, info.desviacion).sample();
    }

    private LocalTime horaAtencion(LocalTime salidaAnterior) {
        if (salidaAnterior == null || salidaAnterior.isBefore(horaLlegada))
            return horaLlegada;
        return salidaAnterior;
    }

    private LocalTime tiempoEspera() {
        return horaLlegada.minusNanos(horaAtencion.toNanoOfDay());
    }

    private Operacion operacion() {
        Data info = Data.getInstance();
        float rand = new Random().nextFloat();
        Operacion resultado = null;

        for (Operacion op : info.operaciones) {
            resultado = op;
            if (op.probabilidad >= rand)
                break;
        }
        return operacion;
    }

    private LocalTime tiempoOperacion() {
        return operacion.duracion;
    }

    private LocalTime horaSalida() {
        return horaLlegada.plusNanos(tiempoOperacion.toNanoOfDay());
    }
}
