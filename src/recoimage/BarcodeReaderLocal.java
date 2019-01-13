package recoimage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.NotFoundException;

public class BarcodeReaderLocal extends BarcodeReader {
	public String readBarcodeSafe(String path)
	{
		try {
			return readBarcode(path);
		} catch (NotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String readBarcode(String path) throws NotFoundException, IOException
	{
		return readBarcode(getImage(path));
	}
	
	private BufferedImage getImage(String path) throws IOException
	{
		return ImageIO.read(new File(path));
	}

}
