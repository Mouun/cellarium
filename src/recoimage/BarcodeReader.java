package recoimage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public abstract class BarcodeReader {
	protected String readBarcodeSafe(BufferedImage img)
	{
		try
		{
			return readBarcode(img);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	protected String readBarcode(BufferedImage img) throws IOException, NotFoundException
	{
		LuminanceSource lumsrc = new BufferedImageLuminanceSource(img);
		BinaryBitmap bmp = new BinaryBitmap(new HybridBinarizer(lumsrc));
		MultiFormatReader readerBC = new MultiFormatReader();
		
		Map<DecodeHintType,Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
		hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		hints.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
		hints.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
		
		Result resultTmp = readerBC.decode(bmp, hints);
		String resultFinal = String.valueOf(resultTmp);
		
		return resultFinal;
	}
}
