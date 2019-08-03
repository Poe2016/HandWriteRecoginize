package handwrite_rec201809;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PicGenData {
	
	
	public PicGenData(){
		
	}
	public int[][] getData(String path){ 
		try{ 
			BufferedImage bimg = ImageIO.read(new File(path)); 
			int [][] data = new int[HwrSurface.ROW_NUM][HwrSurface.COL_NUM];
			int imgWidth=bimg.getWidth();
			int imgHeight=bimg.getHeight();
			for(int i=0;i<imgHeight;i++){ 
				for(int j=0;j<imgWidth;j++){
					if(bimg.getRGB(j,i)==-1){
						data[i][j]=0; 
					}else{
						data[i][j]=1; 
					}
					if(j==HwrSurface.COL_NUM-1)
						break;
				}
				if(i==HwrSurface.ROW_NUM-1)
					break;
			}
			if(imgWidth<HwrSurface.COL_NUM) {
				for(int i=imgWidth;i<HwrSurface.COL_NUM;i++) {
					for(int j=0;j<HwrSurface.ROW_NUM;j++) {
						data[j][i]=0;
					}
				}
			}
			if(imgHeight<HwrSurface.ROW_NUM) {
				for(int i=imgHeight;i<HwrSurface.ROW_NUM;i++) {
					for(int j=0;j<HwrSurface.ROW_NUM;j++) {
						data[i][j]=0;
					}
				}
			}
			return data;
		}catch (IOException e){ 
					e.printStackTrace(); 
		} 
		return null;
		
	} 

}
