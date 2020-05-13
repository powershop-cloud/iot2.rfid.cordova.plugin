package iot2.rfid.cordova.plugin;

import java.util.ArrayList;

/**
 * Interfaz con los metodos a implementar en la actividad en la que se desee recibir los tags leidos por el lector RFID.
 */
public interface ReadTagListener {

    /**
     * Método que se ejecuta cuando se reciben los tags leidos.
     * @param tags Array de cadenas con los tags leidos
     */
    void onReadTags(ArrayList<String> tags);
}
