package controlers;

/**
 * Permet à la fenêtre de lecture de code barre de renvoyer le code-barre lu à une autre fenêtre.
 */
public interface IBarcodeReceiver {
    public void setBarcode(String barcode);
}
