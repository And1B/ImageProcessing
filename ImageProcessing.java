import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageProcessing {
	public static void main(String[] args) {    
    // Get URL from user:
    Scanner sc = new Scanner(System.in)
    System.out.print("Enter the url: ");  
    String url = sc.nextLine();    
      
    // loads image using URL!
		int[][] imageData = imgToTwoD(url);

		// viewImageData(imageData);
    
		int[][] trimmed = trimBorders(imageData, 60);
		twoDToImage(trimmed, "./trimmed_image.jpg");

    int[][] negative = negativeColor(imageData);
		twoDToImage(negative, "./negative_image.jpg");

    int[][] horizontallyStretched = stretchHorizontally(imageData);
    twoDToImage(horizontallyStretched, "./horizontally_stretched_image.jpg");

    int[][] verticallyShrunk = shrinkVertically(imageData);
    twoDToImage(verticallyShrunk, "./vertically_shrunk_image.jpg");

    int[][] invertedImage = invertImage(imageData);
    twoDToImage(invertedImage, "./inverted_image.jpg");

    int[][] colorFilteredImage = colorFilter(imageData, -75, 30, -30);
    twoDToImage(colorFilteredImage, "./color_filtered_image.jpg");
		int[][] allFilters = stretchHorizontally(shrinkVertically(colorFilter(negativeColor(trimBorders(invertImage(imageData), 50)), 200, 20, 40)));
    twoDToImage(allFilters, "./all_filtered_image.jpg");

		// Painting with pixels
    int[][] blankCanvas = new int[500][500];
    int[][] randomImage = paintRandomImage(blankCanvas);
    twoDToImage(randomImage, "./random_image.jpg");

    int[][] anotherBlankCanvas = new int[500][500];
    int[] rgba = {0, 0, 123, 255};
    int[][] rectangleImage = paintRectangle(anotherBlankCanvas, 100, 50, 15, 40, getColorIntValFromRGBA(rgba));
    twoDToImage(rectangleImage, "./rectangle.jpg");

    int[][] newCanvas = new int[500][500];
    int[][] randomRectanglesCanvas = generateRectangles(newCanvas, 1000);
    twoDToImage(randomRectanglesCanvas, "./randomRectanglesCanvas.jpg");
	}

	// Image Processing Methods
	public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
		// Example Method
		if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
			int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
			for (int i = 0; i < trimmedImg.length; i++) {
				for (int j = 0; j < trimmedImg[i].length; j++) {
					trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
				}
			}
			return trimmedImg;
		} else {
			System.out.println("Cannot trim that many pixels from the given image.");
			return imageTwoD;
		}
	}
	public static int[][] negativeColor(int[][] imageTwoD) {
		// TODO: Fill in the code for this method
    int[][] negativeImg = new int[imageTwoD.length][imageTwoD[0].length];
    for (int i = 0; i < imageTwoD.length; i++) {
      for (int j = 0; j < imageTwoD[i].length; j++) {
        int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
        rgba[0] = 255 - rgba[0];
        rgba[1] = 255 - rgba[1];
        rgba[2] = 255 - rgba[2];
        negativeImg[i][j] = getColorIntValFromRGBA(rgba);
      }
    }
    return negativeImg;
  }
	public static int[][] stretchHorizontally(int[][] imageTwoD) {
		int[][] horizontallyStretchedImg = new int[imageTwoD.length][imageTwoD[0].length*2];
    int modifiedColumnIndex = 0;
    for(int rowIndex=0; rowIndex<imageTwoD.length; rowIndex++){
      for(int columnIndex=0; columnIndex<imageTwoD[rowIndex].length;columnIndex++){
        modifiedColumnIndex = columnIndex*2;
        horizontallyStretchedImg[rowIndex][modifiedColumnIndex] = imageTwoD[rowIndex][columnIndex];
        horizontallyStretchedImg[rowIndex][modifiedColumnIndex + 1] = imageTwoD[rowIndex][columnIndex];
      }
    }
    return horizontallyStretchedImg;
	}
	public static int[][] shrinkVertically(int[][] imageTwoD) {
		int[][] verticallyShrunkImg = new int[imageTwoD.length / 2][imageTwoD[0].length];
    for(int columnIndex=0; columnIndex<imageTwoD[0].length; columnIndex++){
      for(int rowIndex=0; rowIndex<imageTwoD.length-1; rowIndex+=2){
        verticallyShrunkImg[rowIndex/2][columnIndex] = imageTwoD[rowIndex][columnIndex];
      }
    }
		return verticallyShrunkImg;
	}
	public static int[][] invertImage(int[][] imageTwoD) {
		int[][] invertedImage = new int[imageTwoD.length][imageTwoD[0].length];
    for(int columnIndex=0; columnIndex<imageTwoD.length;columnIndex++){
      for(int rowIndex=0; rowIndex<imageTwoD[columnIndex].length;rowIndex++){
        invertedImage[columnIndex][rowIndex] = imageTwoD[(imageTwoD.length - 1) - columnIndex][(imageTwoD[columnIndex].length - 1) - rowIndex];
      }
    }
		return invertedImage;
	}
	public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
    int[][] filteredImage = new int[imageTwoD.length][imageTwoD[0].length];
    for(int rowIndex=0; rowIndex<imageTwoD.length; rowIndex++){
      for(int columnIndex=0; columnIndex<imageTwoD[rowIndex].length; columnIndex++){
        int[] rgba_values = getRGBAFromPixel(imageTwoD[rowIndex][columnIndex]);
        rgba_values[0] += redChangeValue;
        if(rgba_values[0] < 0){
          rgba_values[0] = 0;
        } else if (rgba_values[0] > 255){
          rgba_values[0] = 255;
        }
        rgba_values[1] += greenChangeValue;
        if(rgba_values[1] < 0){
          rgba_values[1] = 0;
        } else if (rgba_values[1] > 255){
          rgba_values[1] = 255;
        }
        rgba_values[2] += blueChangeValue;
        if(rgba_values[2] < 0){
          rgba_values[2] = 0;
        } else if (rgba_values[2] > 255){
          rgba_values[2] = 255;
        }
        filteredImage[rowIndex][columnIndex] = getColorIntValFromRGBA(rgba_values);
      }
    }
		return filteredImage;
	}

	// Painting Methods
	public static int[][] paintRandomImage(int[][] canvas) {
    Random rand = new Random();
    for(int rowIndex=0; rowIndex<canvas.length; rowIndex++){
      for(int columnIndex=0; columnIndex<canvas[rowIndex].length; columnIndex++){
        int firstInt = rand.nextInt(256);
        int secondInt = rand.nextInt(256);
        int thirdInt = rand.nextInt(256);
        int[] randomRGBA = { firstInt, secondInt, thirdInt, 255};
        canvas[rowIndex][columnIndex] = getColorIntValFromRGBA(randomRGBA);
      }
    }
		return canvas;
	}
	public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {
		for(int rowIndex=0; rowIndex<canvas.length; rowIndex++){
      for(int columnIndex=0; columnIndex<canvas[rowIndex].length; columnIndex++){
        if((rowIndex >= rowPosition && rowIndex <= rowPosition + width) && (columnIndex >= colPosition && columnIndex <= colPosition + height)) {
          canvas[rowIndex][columnIndex] = color;
        }
      }
    }
		return canvas;
	}
	public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
		Random rand = new Random();
    for(int counter=0; counter < numRectangles; counter++){
      int randomWidth = rand.nextInt(canvas[0].length);
      int randomHeight = rand.nextInt(canvas.length);
      int randomRowPosition = rand.nextInt(canvas.length - randomHeight);
      int randomColumnPosition = rand.nextInt(canvas[0].length - randomWidth);
      int[] randomRGBA = { rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255};
      int randomColor = getColorIntValFromRGBA(randomRGBA);
      canvas = paintRectangle(canvas, randomWidth, randomHeight, randomRowPosition, randomColumnPosition, randomColor);
    }
		return canvas;
	}

	// Utility Methods
	public static int[][] imgToTwoD(String inputFileOrLink) {
		try {
			BufferedImage image = null;
			if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
				URL imageUrl = new URL(inputFileOrLink);
				image = ImageIO.read(imageUrl);
				if (image == null) {
					System.out.println("Failed to get image from provided URL.");
				}
			} else {
				image = ImageIO.read(new File(inputFileOrLink));
			}
			int imgRows = image.getHeight();
			int imgCols = image.getWidth();
			int[][] pixelData = new int[imgRows][imgCols];
			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					pixelData[i][j] = image.getRGB(j, i);
				}
			}
			return pixelData;
		} catch (Exception e) {
			System.out.println("Failed to load image: " + e.getLocalizedMessage());
			return null;
		}
	}
	public static void twoDToImage(int[][] imgData, String fileName) {
		try {
			int imgRows = imgData.length;
			int imgCols = imgData[0].length;
			BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					result.setRGB(j, i, imgData[i][j]);
				}
			}
			File output = new File(fileName);
			ImageIO.write(result, "jpg", output);
		} catch (Exception e) {
			System.out.println("Failed to save image: " + e.getLocalizedMessage());
		}
	}
	public static int[] getRGBAFromPixel(int pixelColorValue) {
		Color pixelColor = new Color(pixelColorValue);
		return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
	}
	public static int getColorIntValFromRGBA(int[] colorData) {
		if (colorData.length == 4) {
			Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
			return color.getRGB();
		} else {
			System.out.println("Incorrect number of elements in RGBA array.");
			return -1;
		}
	}
	public static void viewImageData(int[][] imageTwoD) {
		if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
			int[][] rawPixels = new int[3][3];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rawPixels[i][j] = imageTwoD[i][j];
				}
			}
			System.out.println("Raw pixel data from the top left corner.");
			System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");
			int[][][] rgbPixels = new int[3][3][4];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
				}
			}
			System.out.println();
			System.out.println("Extracted RGBA pixel data from top the left corner.");
			for (int[][] row : rgbPixels) {
				System.out.print(Arrays.deepToString(row) + System.lineSeparator());
			}
		} else {
			System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
		}
	}
}
