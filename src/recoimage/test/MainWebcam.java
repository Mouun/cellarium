package recoimage.test;


import com.github.sarxos.webcam.Webcam;
import recoimage.BarcodeReaderWebcam;

public class MainWebcam {

	public static void main(String[] args) {
		BarcodeReaderWebcam reader = new BarcodeReaderWebcam(Webcam.getDefault());

		System.out.println(reader.readBarcodeSafe());
	}

}
