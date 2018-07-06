

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

public class CreateQRcode {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Qrcode qrcode=new Qrcode();
		qrcode.setQrcodeErrorCorrect('H');//���ö�ά���ݴ���
		qrcode.setQrcodeEncodeMode('B');
		qrcode.setQrcodeVersion(7);//��ά��汾Ϊ6
		String url="http://www.taobao.com";//��ά����ת����ַ
		byte[] data=url.getBytes();
		boolean[][]qrdata=qrcode.calQrcode(data);
		int imageSize=67 + 12*(7-1);//���û����Ŀ�
		//int height=67 + 12*(7-1);//���û����ĸ�
		int pixoff = 4;//����ƫ����
		BufferedImage bufferedImage=new BufferedImage(imageSize,imageSize,BufferedImage.TYPE_INT_RGB);//����ͼƬ������
		Graphics2D gs=bufferedImage.createGraphics();
		gs.setBackground(Color.white);//���ö�ά�뱳��
		gs.clearRect(0,0,imageSize,imageSize);//�������
		
		
		
		int startR=100,startG=0,startB=0;
		int endR=30,endG=88,endB=188;
		
		for(int i=0;i<qrdata.length;i++){//������Ϣ�Ĳ��ֽ������
			for(int j=0;j<qrdata.length;j++){
				if(qrdata[i][j]){
					int num1=startR+(endR-startR)*(j+1)/qrdata.length;
					int num2=startG+(endG-startG)*(i+1)/qrdata.length;          //����ı���䷽�����ɫ
					int num3=startB+(endB-startB)*(j+1)/qrdata.length;
					Color color=new Color(num1,num2,num3);
					gs.setColor(color);
					gs.fillRect(i*3+pixoff/2, j*3+pixoff/2, 3, 3);//ʹ���϶���ƫ����
				}
			}
		}
		
		
		BufferedImage logo=ImageIO.read(new File("D:/peiqi.jpg"));
		int logoSize=imageSize/4;
		int o=(imageSize-logoSize)/2;
		gs.drawImage(logo, o, o, logoSize, logoSize, null); 
		
		gs.dispose();
		bufferedImage.flush();
		try{
		ImageIO.write(bufferedImage, "png", new File("D:/qrcode.png"));
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("���������⡣");
		}
		System.out.println("��ά�����ɳɹ���");
	}
	private static BufferedImage scale(String logoPath,int width,int height,boolean hasFiller) throws Exception{
		double ratio=0.0;
		File file=new File(logoPath);
		BufferedImage srcImage=ImageIO.read(file);
		Image destImage= srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		//�������
		if((srcImage.getHeight()>height)||(srcImage.getWidth()>width)){
			if(srcImage.getHeight()>srcImage.getWidth()){
				ratio=(new Integer(height)).doubleValue()/srcImage.getHeight();
			}else{
				ratio=(new Integer(width)).doubleValue()/srcImage.getWidth();
			}
			AffineTransformOp op=new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio),null);
			destImage=op.filter(srcImage, null);
		}
		if(hasFiller){//����
			BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic=image.createGraphics();
			graphic.setColor(Color.white);
			graphic.fillRect(0, 0, width, height);
			if(width==destImage.getWidth(null)){
				graphic.drawImage(destImage,0,(height-destImage.getHeight(null))/2,destImage.getWidth(null),
				destImage.getHeight(null),Color.white,null);
			}else{
				graphic.drawImage(destImage,(width-destImage.getWidth(null))/2,0,destImage.getWidth(null),
				destImage.getHeight(null),Color.white,null);
			}
			graphic.dispose();
			destImage=image;
			}
		return (BufferedImage) destImage;
		}

	}
