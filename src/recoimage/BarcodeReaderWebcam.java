package recoimage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.NotFoundException;

public class BarcodeReaderWebcam extends BarcodeReader {
	private Webcam webcam;
	private BufferedImage cachedImage;
	
	public BarcodeReaderWebcam()
	{
		this(Webcam.getDefault());
	}
	
	public BarcodeReaderWebcam(Webcam webcam)
	{
		this.webcam = webcam;
	}
	
	public String readBarcodeSafe()
	{
		try {
			return readBarcode();
		} catch (NotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String readBarcode() throws NotFoundException, IOException
	{
		if (cachedImage == null) takeImage();
		return readBarcode(cachedImage);
	}
	
	public BufferedImage takeImage()
	{
		cachedImage = webcam.getImage();
		return cachedImage;
	}

}
