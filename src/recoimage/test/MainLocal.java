package recoimage.test;

import recoimage.BarcodeReaderLocal;

public class MainLocal {

	public static void main(String[] args) {
		BarcodeReaderLocal reader = new BarcodeReaderLocal();

		System.out.println(reader.readBarcodeSafe("code_clair.png"));
		System.out.println(reader.readBarcodeSafe("code_photo.jpg"));
		System.out.println(reader.readBarcodeSafe("code_courbe.jpg"));
	}
}
