package iot2.rfid.cordova.plugin;

/**
 * Interfaz con los metodos a implementar en la actividad en la que se desee recibir los códigos de barras leidos por el lector.
 */
public interface ReadBarcodeListener {

    /**
     * Método que se ejecuta cuando se recibe el código de barras leido.
     * @param barcode Cadena con el código de barras.
     */
    void onReadBarcode(String barcode);
}
