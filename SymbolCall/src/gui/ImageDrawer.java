package gui;

import java.awt.Graphics;
import java.awt.Image;

public class ImageDrawer {
	public static void drawImage(Image image, int x, int y, int width, int height, Graphics graphics){
		if(image!=null) {
			double neededRatio = (double) height / (double) width;
			double imageRatio = (double) image.getHeight(null) / (double) image.getWidth(null);
			int sourceX1 = 0;
			int sourceY1 = 0;
			int sourceX2 = image.getWidth(null);
			int sourceY2 = image.getHeight(null);
			if (imageRatio > neededRatio) {
				int sourceHeight = (int) ((double) image.getWidth(null) * neededRatio);
				sourceY1 = (image.getHeight(null) - sourceHeight) / 2;
				sourceY2 = sourceY1 + sourceHeight;
			} else {
				int sourceWidth = (int) ((double) image.getHeight(null) / neededRatio);
				sourceX1 = (image.getWidth(null) - sourceWidth) / 2;
				sourceX2 = sourceX1 + sourceWidth;
			}
			graphics.drawImage(image, x, y, x + width, y + height, sourceX1,
					sourceY1, sourceX2, sourceY2, null);
		}
	}
}
